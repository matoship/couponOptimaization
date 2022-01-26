import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

class item {
    int id;
    int val;
    int count;
    int price;

    public item ( int id , int price , int count ) {
        this.id = id;
        this.price = price;
        this.count = count;
        this.val = price * count;
    }

    @Override
    public String toString ( ) {
        return "item{" +
                "id=" + id +
                ", total value=" + val +
                ", count=" + count +
                ", price=" + price +
                '}';
    }
}

class coupon implements Comparable<coupon> {
    int[] combination;
    int val;
    int threshold;

    public coupon ( int[] combination , int val , int threshold ) {
        this.combination = combination;
        this.val = val;
        this.threshold = threshold;
    }

    public boolean checkValidity ( int id ) {
        for ( int j : combination ) {
            if ( id == j && id != 0 ) return true;
        }

        return false;
    }


    @Override
    public String toString ( ) {
        return "coupon{" +
                "combination=" + Arrays.toString ( combination ) +
                ", val=" + val +
                ", threshold=" + threshold +
                '}';
    }

    public int compareTo ( coupon other ) {
        return other.val - this.val;
    }
}

//driver
public class Main {
    public static int getRandomNumber ( int max , int min ) {
        Random random = new Random ( );
        return random.nextInt ( max - min ) + min;
    }

    public static final int NUMITEMS = 99;
    public static final int NUMCOUPONS = 1000;

    public static void main ( String[] args ) {


        int DISCOUNT = 50;
        int MAXPRICE = 1000;
        int MINPRICE = 100;
        int MAXCOUNT = 1000;
        //set items
        item[] items = new item[NUMITEMS];
        for ( int i = 0 ; i < NUMITEMS ; i++ ) {
            items[i] = new item ( i , getRandomNumber ( MAXPRICE , MINPRICE ) , getRandomNumber ( MAXCOUNT , 1 ) );
        }
        //set coupons;
        coupon[] coupons=new coupon[NUMCOUPONS];
        for(int i =0; i<NUMCOUPONS;i++){
            int[] combs = new int[getRandomNumber(NUMITEMS,1)];
            for(int j=0;j<combs.length;j++){
                combs[j]=(getRandomNumber(NUMITEMS,1));
            }

            coupons[i]=new coupon(combs,getRandomNumber(DISCOUNT,1),getRandomNumber(100,1));
        }
//        for(int i=0;i<NUMITEMS;i++) {
//            System.out.println(items[i].toString());
//        }

//        int[] a1 = { 1 };
//        int[] a2 = { 1 , 2 };
//        int[] a3 = { 2 , 3 };
//        int[] a4 = { 3 , 4 };
//        int[] a5 = { 1 , 4 };
//        int[] a6 = { 2 };
//        int[] a7 = { 30 };
//        int[] a8 = { 4 };
//        coupon[] coupons = new coupon[5];
//        coupons[0] = new coupon ( a1 , 3 , 5 );
//        coupons[1] = new coupon ( a6 , 4 , 20 );
//        coupons[2] = new coupon ( a7 , 5 , 15 );
//        coupons[3] = new coupon ( a8 , 6 , 10 );
//        coupons[4] = new coupon ( a5 , 50 , 14 );

//        for(int i=0;i<NUMCOUPONS;i++) {
//            System.out.println(coupons[i].toString());
//        }

        HashSet<coupon> validCoupon = new HashSet<> ( );
        couponOptimization ( items , coupons , validCoupon );
        coupon[] coup = new coupon[validCoupon.size()];
        validCoupon.toArray (coup);
        Arrays.sort ( coup );
//        Iterator i = validCoupon.iterator ();
//        while(i.hasNext ()){
//            System.out.println(i.next ().toString ());
//        }
        for(coupon x: coup){
            System.out.println ( x.toString () );
        }
        int result = getBestDiscount ( coup );
        System.out.println ( result );
    }

    public static void addTOSet ( int[] comb , HashSet<Integer> set ) {
        for ( int j : comb ) {
            set.add ( j );
        }
    }

    public static boolean isOverlapping ( int[] Comb , HashSet<Integer> set ) {
        for ( int j : Comb ) {
            if ( ( set.contains ( j ) ) ) {
                return true;
            }
        }
        return false;
    }


    public static void couponOptimization ( item[] items , coupon[] coupons , HashSet<coupon> validCoupon ) {
        //check if the coupon has the item included in its combination.
        for ( int i = 0 ; i < items.length ; i++ ) {
            for ( int j = 0 ; j < coupons.length ; j++ ) {
                //if the item is included, check if it meets the price threshold.
                if ( coupons[j].checkValidity ( items[i].id ) && !validCoupon.contains ( coupons[j] )) {
                    if ( items[i].val >= coupons[j].threshold ) {
                        validCoupon.add ( coupons[j] );
                    }
                    else {
                        int sum = items[i].val;
                        for (int u = i+1;u<items.length;u++ ){
                            if(coupons[j].checkValidity ( items[u].id )){
                                if(sum+items[u].val>=coupons[j].threshold){
                                    validCoupon.add(coupons[j]);
                                    break;
                                }
                                else{
                                    sum=+items[u].val;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static int getBestDiscount ( coupon[] coupons ) {
        int bestDiscount = 0;
        for ( int i = 0 ; i < coupons.length ; i++ ) {
            int temp = coupons[i].val;
            HashSet<Integer> set = new HashSet<> ( );
            addTOSet ( coupons[i].combination , set );
            for ( int j = i + 1 ; j < coupons.length ; j++ ) {
                if ( ! isOverlapping ( coupons[j].combination , set ) ) {
                    addTOSet ( coupons[j].combination , set );
                    temp += coupons[j].val;
                }
//                System.out.println ( "base on coupon " + i + " " + coupons[i].toString ( ) +
//                        " calculating best combination with coupon " + j + " " + coupons[j].toString ( )
//                        + "current best offer is " + temp );
            }
            bestDiscount = Math.max ( temp , bestDiscount );

        }
        return bestDiscount;
    }

}
