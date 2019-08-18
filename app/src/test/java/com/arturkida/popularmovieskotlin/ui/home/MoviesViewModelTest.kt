package com.arturkida.popularmovieskotlin.ui.home

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.arturkida.popularmovieskotlin.BaseUnitTest
import com.arturkida.popularmovieskotlin.data.local.MovieRepository
import com.arturkida.popularmovieskotlin.data.remote.ApiImpl
import com.arturkida.popularmovieskotlin.ui.MoviesViewModel
import com.arturkida.popularmovieskotlin.utils.SearchType
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class MoviesViewModelTest : BaseUnitTest() {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var movieRepository: MovieRepository
    @MockK
    private lateinit var apiImpl: ApiImpl

    private lateinit var moviesViewModel: MoviesViewModel

    @Before
    fun setup() {
        // Given
        MockKAnnotations.init(this)

        every { apiImpl.getGenres() } answers { getGenresList() }
        every { apiImpl.getPopularMovies() } answers { getSingleLiveDataResourceFavoriteMovieList() }
        every { movieRepository.getMoviesFromDatabase() } answers { getSingleLiveDataFavoriteMovieList() }

        moviesViewModel = spyk(MoviesViewModel(movieRepository, apiImpl))
    }

    @Test
    fun `must show movies list when a list is received`() {
        // Given
        val testList = getSingleMovieList()

        // When
        val testResult = moviesViewModel.mustShowMoviesList(testList)

        // Then
        assertThat(testResult, equalTo(true))
    }

    @Test
    fun `must show no movies found when an empty list is received`() {
        // When
        val testResult = moviesViewModel.mustShowMoviesList(listOf())

        // Then
        assertThat(testResult, equalTo(false))
    }

    @Test
    fun `must update a favorite movie status`() {
        // Given
        val testMovie = getSingleMovieList()

        every { moviesViewModel.isMovieFavorited(testMovie[0]) } returns true

        // When
        val testResult = moviesViewModel.updateFavoriteStatusOf(testMovie[0])

        // Then
        assertThat(testResult.favorite, equalTo(true))
    }

    @Test
    fun `must keep a non favorite movie status`() {
        // Given
        val testMovie = getSingleMovieList()

        every { moviesViewModel.isMovieFavorited(testMovie[0]) } returns false

        // When
        val testResult = moviesViewModel.updateFavoriteStatusOf(testMovie[0])

        // Then
        assertThat(testResult.favorite, equalTo(false))
    }

    @Test
    fun `must filter movie list by title and only return Avengers movie`() {
        // Given
        val testMovie = getMovieListWithAvengersAndLionKing()

        // When
        val testResult = moviesViewModel.searchMovies("Avengers", SearchType.TITLE, testMovie)

        // Then
        assertThat(testResult[0].title, equalTo("Avengers: Endgame"))
        assertThat(testResult.size, equalTo(1))
    }

    @Test
    fun `must filter movie list by title and return an empty list`() {
        // Given
        val testMovie = getMovieListWithAvengersAndLionKing()

        // When
        val testResult = moviesViewModel.searchMovies("Aladdin", SearchType.TITLE, testMovie)

        // Then
        assertThat(testResult.size, equalTo(0))
    }
}