package app.bettermetesttask.datamovies.repository

import app.bettermetesttask.datamovies.database.entities.MovieEntity
import app.bettermetesttask.datamovies.repository.stores.MoviesLocalStore
import app.bettermetesttask.datamovies.repository.stores.MoviesMapper
import app.bettermetesttask.datamovies.repository.stores.MoviesRestStore
import app.bettermetesttask.domainmovies.repository.MoviesRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
internal class MoviesRepositoryTest {

    private val localStore: MoviesLocalStore = mock()
    private val restStore: MoviesRestStore = mock()
    private val mapper: MoviesMapper = mock()
    private val moviesRepository: MoviesRepository = MoviesRepositoryImpl(localStore, restStore, mapper)

    @Test
    fun `getMovie should call getMovie method in MoviesLocalStore`() = runTest {
        val movieId = 1

        moviesRepository.getMovie(movieId)

        verify(localStore).getMovie(movieId)
    }

    @Test
    fun `addMovieToFavorites should call likeMovie method in MoviesLocalStore`() = runTest {
        val movieId = 1

        moviesRepository.addMovieToFavorites(movieId)

        verify(localStore).likeMovie(movieId)
    }

    @Test
    fun `removeMovieFromFavorites should call dislikeMovie method in MoviesLocalStore`() = runTest {
        val movieId = 1

        moviesRepository.removeMovieFromFavorites(movieId)

        verify(localStore).dislikeMovie(movieId)
    }

    @Test
    fun `observeLikedMovieIds should call observeLikedMoviesIds method in MoviesLocalStore`() = runTest {

        moviesRepository.observeLikedMovieIds()

        verify(localStore).observeLikedMoviesIds()
    }

    @Test
    fun `getMovies should call getMovies method in MoviesRestStore if local database empty`() = runTest {
        val index = 0
        val movieEntity = MovieEntity(
            index,
            "Title $index",
            "Overview $index",
            null
        )

        whenever(restStore.getMovies()).thenReturn(listOf<MovieEntity>(movieEntity))
        whenever(localStore.getMovies()).thenReturn(emptyList<MovieEntity>())

        moviesRepository.getMovies()

        verify(restStore).getMovies()
    }

    @Test
    fun `getMovies should call getMovies method in MoviesLocalStore if local database not empty`() = runTest {
        val index = 0
        val movieEntity = MovieEntity(
            index,
            "Title $index",
            "Overview $index",
            null
        )

        whenever(localStore.getMovies()).thenReturn(listOf<MovieEntity>(movieEntity))

        moviesRepository.getMovies()

        verify(localStore).getMovies()
    }


    // To mock dependencies - you can use the following syntax
    //    private val someClass: String = mock()

    // To test suspend function - you can use `runTest`
    //    @Test
    //    fun `test suspend function`() = runTest {
    //       Verify something
    //
    //    }

    // To verify a method is called - you can use the following syntax
    //    verify(someObj).someMethod()

}