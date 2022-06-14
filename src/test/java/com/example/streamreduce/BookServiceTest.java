package com.example.streamreduce;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByKey;
import static java.util.stream.Collectors.*;

public class BookServiceTest {

    BookService bookService  = new BookService();

    @Test

    public void testAddReview(){
        Book book1 = new Book("book1","auth1", BookType.FICTION, BigDecimal.valueOf(34.56));
        Review review1 = new Review(4, "review 1");
        bookService.addReview(book1, review1);
    }
    
    @Test  //book1=3.5
    public void createBookOverallRating(){
        List<Book> books = createBookList();
        Map<String, Double> bookRatingMap = books.stream()
                .collect(toMap(Book::getBookName, b -> b.computeRating(b.getReviews())))
                .entrySet().stream().sorted(comparingByKey())
                .peek(System.out::println)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2)->e1, HashMap::new));
    }

    //find the highest price
    @Test
    public void highestPrice(){
        List<Book> books = createBookList();
        Comparator<BigDecimal> c = Comparator.naturalOrder();
        Optional<BigDecimal> highestPriceOpt = books.stream()
                .map(Book::getPrice).max(c);
        BigDecimal highestPrice = highestPriceOpt.map(bigDecimal -> bigDecimal.setScale(2, RoundingMode.HALF_UP)).orElse(null);
        System.out.println(highestPrice);
    }

    //find the highest prices book
    @Test
    public void highestPricedBook(){
        Comparator<BigDecimal> c = Comparator.naturalOrder();
        List<Book> books = createBookList();
        Optional<BigDecimal> highestPriceOpt = books.stream().map(Book::getPrice).max(c);
        Map<String, BigDecimal> collect = books.stream()
                .filter((Book b) -> b.getPrice().compareTo(highestPriceOpt.get()) == 0)
                .collect(toMap(Book::getBookName, Book::getPrice));
        System.out.println(collect);
    }

    //list bookName and review string, sorted by bookName
    @Test
    public void bookNameAndReviewStringSortedByBookName(){
        List<Book> books = createBookList();
//        Comparator<Map.Entry<String, String>> c = (Map.Entry<String, String> e1, Map.Entry<String, String> e2)->{
//            String bookName1 =(String) e1.getKey();
//            String bookName2 = (String) e2.getKey();
//            return bookName1.compareTo(bookName2);
//        };
        Map<String, String> bookNameAndReviewString = books.stream()
                .collect(Collectors.toMap(Book::getBookName, (Book b)->b.joinReview(b.getReviews())))
                .entrySet().stream().sorted(comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
        System.out.println(bookNameAndReviewString);
    }

    //groupingBy
    @Test
    public void groupByBookType(){
        List<Book> books = createBookList();
        Map<BookType, List<Book>> collect = books.stream()
                .collect(groupingBy(Book::getBookType));
        System.out.println(collect);
    }

    @Test
    public void groupByBookType_SortedByReviewThenBookType(){
        List<Book> books = createBookList();
        List<Book> sortedReviewBooks = books.stream()
                .peek((Book b)-> b.setReviews(b.sortReview(b.getReviews()))).collect(toList());
        Map<BookType, List<Book>> collect = sortedReviewBooks.stream()
                .collect(groupingBy(Book::getBookType))
                .entrySet().stream().sorted(comparingByKey())
                .peek(System.out::println)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2)->e1, HashMap::new));
    }

    @Test
    public void testSortReviews(){
       List<Book> books = createBookList();
        List<Book> collect = books.stream().peek((Book b) -> b.setReviews(b.sortReview(b.getReviews()))).peek(System.out::println)
                .collect(toList());

    }


    private List<Book> createBookList(){
        Book book1 = new Book("book1","auth1", BookType.FICTION, BigDecimal.valueOf(34.56));
        Book book2 = new Book("book2","auth2", BookType.JOURNAL, BigDecimal.valueOf(904.36));
        Book book3 = new Book("book3","auth3", BookType.BIOGRAPHY, BigDecimal.valueOf(54.09));
        Book book4 = new Book("book4","auth4", BookType.BIOGRAPHY, BigDecimal.valueOf(23.98));
        Book book5 = new Book("book5","auth4", BookType.FICTION, BigDecimal.valueOf(101.66));
        Book book6 = new Book("book6","auth2", BookType.BIOGRAPHY, BigDecimal.valueOf(12.00));

        Review review1 = new Review(4, "review 1");
        Review review2 = new Review(5, "review 2");
        Review review3 = new Review(0, "review 3");
        Review review4 = new Review(2, "review 4");
        Review review5 = new Review(1, "review 5");
        Review review6 = new Review(3, "review 6");

        bookService.addReview(book1, review1);
        bookService.addReview(book1, review2);
        bookService.addReview(book2, review3);
        bookService.addReview(book3, review4);
        bookService.addReview(book3, review5);
        bookService.addReview(book4, review6);
        bookService.addReview(book5, review1);
        bookService.addReview(book6, review2);

        return Arrays.asList(book1, book2, book3, book4, book5, book6);
    }

}
