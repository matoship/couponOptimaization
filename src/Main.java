import java.util.*;


class item {
    int id;
    int val;
    int count;
    int price;

    public item(int id) {
        this.id = id;
    }

    public item(int id, int price, int count) {
        this.id = id;
        this.price = price;
        this.count = count;
        this.val = price * count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        item item = (item) o;
        return id == item.id && val == item.val && count == item.count && price == item.price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, val, count, price);
    }

    @Override
    public String toString() {
        return "item{" +
                "id=" + id +
                ", total value=" + val +
                ", count=" + count +
                ", price=" + price +
                '}';
    }
}

class coupon implements Comparable<coupon> {

    List<item> combination = new ArrayList<>();
    int discount;
    int threshold;
    int id;

    public coupon() {
    }

    public coupon(int id, List<item> combination, int val, int threshold) {
        this.id = id;
        this.combination = combination;
        this.discount = val;
        this.threshold = threshold;
    }

    public boolean checkValidity(item good) {
        for (item item : combination) {
            if (good.id == item.id && item.id != 0) return true;
        }

        return false;
    }

    public void cloneCoupon(coupon other, List<item> list) {
        this.id = other.id;
        this.threshold = other.threshold;
        this.discount = other.discount;
        this.combination = list;
    }

    @Override
    public String toString() {
        return "coupon{" +
                "combination=" + combination +
                ", discount=" + discount +
                ", threshold=" + threshold +
                ", id=" + id +
                '}';
    }

    public int compareTo(coupon other) {
        return other.discount - this.discount;
    }
}

//driver
public class Main {
    public static int getRandomNumber(int max, int min) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public static int getRandomNumber(int max, int min,HashSet<Integer> set) {
        Random random = new Random();
        int tmp = random.nextInt(max - min) + min;
        if(set.add(tmp)){
            return(tmp);
        }
        return getRandomNumber(max, min, set);

    }

    public static final int SHOPPINGCART = 99;
    public static final int NUMCOUPONS = 1000;
    public static final int NUMITEMS = 100000;

    public static void main(String[] args) {


        int DISCOUNT = 50;
        int MAXPRICE = 1000;
        int MINPRICE = 100;
        int MAXCOUNT = 100;
        //set all items
        item[] allItems = new item[NUMITEMS];
        item[] items = new item[SHOPPINGCART];
        for (int i = 0; i < NUMITEMS; i++) {
            allItems[i] = new item(i, getRandomNumber(MAXPRICE, MINPRICE), getRandomNumber(MAXCOUNT, 1));
        }

        HashSet<Integer> nums = new HashSet<>();
        for (int i = 0; i < SHOPPINGCART; i++) {
            int tmp = 0;
            tmp = getRandomNumber(NUMITEMS, 1,nums);
            items[i]=allItems[tmp];
        }
        nums.clear();

        //set coupons;
        coupon[] coupons = new coupon[NUMCOUPONS];
        for (int i = 0; i < NUMCOUPONS; i++) {
            List<item> combs = new ArrayList<>();
            for (int j = 0; j < getRandomNumber(100, 1); j++) {
                combs.add(allItems[getRandomNumber(NUMITEMS,1,nums)]);
            }
            nums.clear();
            coupons[i] = new coupon(i, combs, getRandomNumber(DISCOUNT, 1), getRandomNumber(100, 1));
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

        List<coupon> validCoupon = new ArrayList<>();
        couponRangeShrink(items, coupons, validCoupon);
        //Arrays.sort ( validCoupon );
//        Iterator i = validCoupon.iterator ();
//        while(i.hasNext ()){
//            System.out.println(i.next ().toString ());
//        }
        for (coupon x : validCoupon) {
            System.out.println(x.toString());
        }
//        int result = getBestDiscount ( coup );
//        System.out.println ( result );
    }

    public static void addTOSet(int[] comb, HashSet<Integer> set) {
        for (int j : comb) {
            set.add(j);
        }
    }

    public static boolean isOverlapping(int[] Comb, HashSet<Integer> set) {
        for (int j : Comb) {
            if ((set.contains(j))) {
                return true;
            }
        }
        return false;
    }

    public static void couponRangeShrink(item[] items, coupon[] coupons, List<coupon> validCoupon) {
        for (coupon coupon : coupons) {
            List<item> newList = new ArrayList<>();
            coupon temp = new coupon();
            int sum = 0;
            for (item item : items) {
                if (coupon.checkValidity(item)) {
                    sum = +item.val;
                    newList.add(item);
                }
            }
            if (newList.size() > 0 && sum >= coupon.threshold) {
                temp.cloneCoupon(coupon, newList);
                validCoupon.add(temp);
            }
        }
    }

//    public static void couponOptimization ( item[] items , coupon[] coupons , HashSet<coupon> validCoupon ) {
//        //check if the coupon has the item included in its combination.
//        for ( int i = 0 ; i < items.length ; i++ ) {
//            for ( int j = 0 ; j < coupons.length ; j++ ) {
//                //if the item is included, check if it meets the price threshold.
//                if ( coupons[j].checkValidity ( items[i].id ) && !validCoupon.contains ( coupons[j] )) {
//                    if ( items[i].val >= coupons[j].threshold ) {
//                        validCoupon.add ( coupons[j] );
//                    }
//                    else {
//                        int sum = items[i].val;
//                        for (int u = i+1;u<items.length;u++ ){
//                            if(coupons[j].checkValidity ( items[u].id )){
//                                if(sum+items[u].val>=coupons[j].threshold){
//                                    validCoupon.add(coupons[j]);
//                                    break;
//                                }
//                                else{
//                                    sum=+items[u].val;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

//    public static int getBestDiscount ( coupon[] coupons ) {
//        int bestDiscount = 0;
//        for ( int i = 0 ; i < coupons.length ; i++ ) {
//            int temp = coupons[i].val;
//            HashSet<Integer> set = new HashSet<> ( );
//            addTOSet ( coupons[i].combination , set );
//            for ( int j = i + 1 ; j < coupons.length ; j++ ) {
//                if ( ! isOverlapping ( coupons[j].combination , set ) ) {
//                    addTOSet ( coupons[j].combination , set );
//                    temp += coupons[j].val;
//                }
////                System.out.println ( "base on coupon " + i + " " + coupons[i].toString ( ) +
////                        " calculating best combination with coupon " + j + " " + coupons[j].toString ( )
////                        + "current best offer is " + temp );
//            }
//            bestDiscount = Math.max ( temp , bestDiscount );
//
//        }
//        return bestDiscount;
//    }

}
