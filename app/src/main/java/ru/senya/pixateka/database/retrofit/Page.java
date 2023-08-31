package ru.senya.pixateka.database.retrofit;


public class Page<T> {

    public T content;
    public Pageable pageable;
    public boolean last;
    public int totalPages;
    public int totalElements;
    public int size;
    public int number;
    public Sort sort;
    public boolean first;
    public int numberOfElements;
    public boolean empty;

    public class Pageable{
        public Sort sort;
        public int offset;
        public int pageNumber;
        public int pageSize;
        public boolean paged;
        public boolean unpaged;
    }

    public class Sort   {
        public boolean empty;
        public boolean sorted;
        public boolean unsorted;
    }


}
