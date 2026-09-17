package com.driver.bookMyShow.Repositories;

import com.driver.bookMyShow.Enums.Language;
import com.driver.bookMyShow.Models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

    boolean existsByMovieNameIgnoreCaseAndLanguage(
            String movieName,
            Language language
    );
}