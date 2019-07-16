package com.spring.resource;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.spring.model.CatelogItem;
import com.spring.model.Movie;
import com.spring.model.Rating;
import com.spring.model.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catelog")
public class MovieCatelogResource {

    @Autowired
    WebClient.Builder webClientBuilder;

    @Autowired
    RestTemplate restTemplate;
    @RequestMapping("/{userId}")
    public List<CatelogItem> getCatelog(@PathVariable("userId") String userId){

        UserRating ratings=restTemplate.getForObject("http://MoviRating/rating/user/"+userId, UserRating.class);
        return ratings.getUserRating().stream().map(rating -> {
           Movie movie= restTemplate.getForObject("http://MoviInfo/movie/"+rating.getMovieId(), Movie.class);
         /*Movie movie= webClientBuilder.build()
                  .get()
                  .uri("http://localhost:8081/movie/"+rating.getMovieId())
                  .retrieve()
                  .bodyToMono(Movie.class)
                  .block();
*/

            return new CatelogItem(movie.getName(),"Desc",rating.getRating());
        })
                .collect(Collectors.toList());
    }

}
