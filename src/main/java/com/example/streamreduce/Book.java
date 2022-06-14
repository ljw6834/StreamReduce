package com.example.streamreduce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class Book {
    String bookName;
    String author;
    BookType bookType;
    BigDecimal price;
    List<Review> reviews;

    public Book(String bookName, String auth, BookType bookType, BigDecimal price){
        this.bookName = bookName;
        this.author = auth;
        this.bookType = bookType;
        this.price = price;
    }

    public double computeRating(List<Review> reviews){
        int total = reviews.stream().map(Review::getPoints).reduce(0, Integer::sum);
        long count = reviews.stream().count();
        return total/count;
    }

    public String joinReview(List<Review> reviews){
        String reviewString =reviews.stream()
                .map((Review r)->r.getComment()).collect(Collectors.joining(":"));
        return reviewString;
    }

    public List<Review> sortReview(List<Review> reviews){
        List<Review> sortedReviews = reviews.stream().sorted(Comparator.comparingInt(Review::getPoints).reversed())
                .collect(Collectors.toList());
        return sortedReviews;
    }
}
