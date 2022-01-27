import java.util.ArrayList;
import java.util.List;

class coupon implements Comparable<coupon> {

    List<item> combination = new ArrayList<> ( );
    int discount;
    int threshold;
    int id;

    public coupon ( ) {
    }

    public coupon ( int id , List<item> combination , int val , int threshold ) {
        this.id = id;
        this.combination = combination;
        this.discount = val;
        this.threshold = threshold;
    }

    public boolean checkValidity ( item good ) {
        for ( item item : combination ) {
            if ( good.id == item.id && item.id != 0 ) return true;
        }

        return false;
    }

    public void cloneCoupon ( coupon other , List<item> list ) {
        this.id = other.id;
        this.threshold = other.threshold;
        this.discount = other.discount;
        this.combination = list;
    }

    @Override
    public String toString ( ) {
        return "coupon{" +
                "combination=" + combination +
                ", discount=" + discount +
                ", threshold=" + threshold +
                ", id=" + id +
                '}';
    }

    public int compareTo ( coupon other ) {
        return other.discount - this.discount;
    }
}
