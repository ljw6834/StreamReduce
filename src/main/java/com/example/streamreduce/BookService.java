package com.example.streamreduce;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {

    Map<Book, List<Review>> bookReview = new HashMap<Book, List<Review>>();
    List<Book> bookList = new ArrayList<>();

    public void addReview(Book book, Review review) {
        List<Review> reviewList;
        Optional<Book> bookOpt = bookList.
                stream()
                .filter(b -> StringUtils.equalsIgnoreCase(b.getBookName(), book.getBookName()))
                .findAny();
        if (bookOpt.isPresent()) {
            reviewList = bookOpt.get().getReviews();
            reviewList.add(review);
            book.setReviews(reviewList);
        } else {
            reviewList = new ArrayList<>();
            reviewList.add(review);
            book.setReviews(reviewList);
            bookList.add(book);
        }
    }

    public Map<Book, List<Review>> getAllBookReview(){
        Map<Book, List<Review>> bookReviewsMap = bookList.stream()
                .collect(Collectors.toMap(b->b, b->b.getReviews()));
        return bookReviewsMap;
    }

    public List<Review> getReviewByBook(Book book){
        return bookReview.get(book);
    }





}
