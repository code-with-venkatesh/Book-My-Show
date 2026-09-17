package com.driver.bookMyShow.Services;

import com.driver.bookMyShow.Dtos.RequestDtos.MovieEntryDto;
import com.driver.bookMyShow.Exceptions.MovieAlreadyPresentWithSameNameAndLanguage;
import com.driver.bookMyShow.Exceptions.MovieDoesNotExists;
import com.driver.bookMyShow.Models.Movie;
import com.driver.bookMyShow.Models.Show;
import com.driver.bookMyShow.Models.Ticket;
import com.driver.bookMyShow.Repositories.MovieRepository;
import com.driver.bookMyShow.Repositories.ShowRepository;
import com.driver.bookMyShow.Transformers.MovieTransformer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final ShowRepository showRepository;

    public MovieService(
            MovieRepository movieRepository,
            ShowRepository showRepository
    ) {
        this.movieRepository = movieRepository;
        this.showRepository = showRepository;
    }

    @Transactional
    public String addMovie(MovieEntryDto movieEntryDto) {
        boolean movieExists =
                movieRepository.existsByMovieNameIgnoreCaseAndLanguage(
                        movieEntryDto.getMovieName(),
                        movieEntryDto.getLanguage()
                );

        if (movieExists) {
            throw new MovieAlreadyPresentWithSameNameAndLanguage();
        }

        Movie movie = MovieTransformer.movieDtoToMovie(movieEntryDto);
        movieRepository.save(movie);

        return "The movie has been added successfully";
    }

    @Transactional(readOnly = true)
    public Long totalCollection(Integer movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new MovieDoesNotExists();
        }

        List<Show> shows = showRepository.getAllShowsOfMovie(movieId);

        long amount = 0L;

        for (Show show : shows) {
            for (Ticket ticket : show.getTicketList()) {
                amount += ticket.getTotalTicketsPrice();
            }
        }

        return amount;
    }
}