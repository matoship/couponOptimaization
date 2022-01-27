import java.util.Objects;

class item implements Comparable<item> {
    int id;
    int val;
    int count;
    int price;

    public item ( int id ) {
        this.id = id;
    }

    public item ( int id , int price , int count ) {
        this.id = id;
        this.price = price;
        this.count = count;
        this.val = price * count;
    }

    @Override
    public boolean equals ( Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass ( ) != o.getClass ( ) ) return false;
        item item = (item) o;
        return id == item.id && val == item.val && count == item.count && price == item.price;
    }

    @Override
    public int hashCode ( ) {
        return Objects.hash ( id , val , count , price );
    }

    @Override
    public String toString ( ) {
        return "item{" +
                "id=" + id +
                ", total value=" + val +
//                ", count=" + count +
//                ", price=" + price +
                '}';
    }

    @Override
    public int compareTo ( item o ) {
        return this.id-o.id;
    }
}
