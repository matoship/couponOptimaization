import java.util.*;

//class item{
//    int id;
//    int val;
//    //int nums;
//    public item() {
//        this.id = 0;
//        this.val = 0;
//    }
//    public item(int id, int val) {
//        this.id = id;
//        this.val = val;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public void setVal(int val) {
//        this.val = val;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public int getVal() {
//        return val;
//    }
//
//    @Override
//    public String toString() {
//        return "item{" +
//                "id=" + id +
//                ", val=" + val +
//                '}';
//    }
//}
class coupon implements Comparable<coupon> {
    int[] combination;
    int val;

    public coupon(int[] combination, int val) {
        this.combination = combination;
        this.val = val;
    }

    public boolean checkValidity(int id) {
        for (int i = 0; i < combination.length; i++) {
            if (id == combination[i] && id != 0) return true;
        }
        return false;
    }

    public int getLength() {
        return this.combination.length;
    }

    public int getItemId(int index) {
        return this.combination[index];
    }

    @Override
    public String toString() {
        return "coupon{" +
                "combination=" + Arrays.toString(combination) +
                ", val=" + val +
                '}';
    }

    public int compareTo(coupon other) {
        return other.val - this.val;
    }
}

//driver
public class Main {
    public static int getRandomNumber(int max, int min) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public static final int NUMITEMS = 99;
    public static final int NUMCOUPONS = 3;

    public static void main(String[] args) {


//        int DISCOUNT=50;
//        int MAXPRICE = 1000;
//        int MINPRICE = 100;
//        //set items
//        item[] items = new item[NUMITEMS];
//        for(int i =0;i<NUMITEMS;i++){
//            items[i]=new item(i,getRandomNumber(MAXPRICE,MINPRICE));
//        }
//        //set coupons;
//        coupon[] coupons=new coupon[NUMCOUPONS];
//        for(int i =0; i<NUMCOUPONS;i++){
//            int[] combs = new int[NUMITEMS];
//            for(int j=0;j<getRandomNumber(NUMCOUPONS,1);j++){
//                combs[j]=(getRandomNumber(NUMITEMS,1));
//            }
//
//            coupons[i]=new coupon(combs,getRandomNumber(DISCOUNT,1));
//        }
//        for(int i=0;i<NUMITEMS;i++) {
//            System.out.println(items[i].toString());
//        }
        int[] items = {1, 2, 3, 4, 5, 6, 7};
        int[] a1 = {1};
        int[] a2 = {1, 2};
        int[] a3 = {2, 3};
        int[] a4 = {3, 4};
        int[] a5 = {1, 2, 3, 4, 7};
        int[] a6 = {2};
        int[] a7 = {3};
        int[] a8 = {4};
        coupon[] coupons = new coupon[5];
        coupons[0] = new coupon(a1, 3);
        coupons[1] = new coupon(a6, 4);
        coupons[2] = new coupon(a7, 5);
        coupons[3] = new coupon(a8, 6);
        coupons[4] = new coupon(a5, 10);
        //Arrays.sort(coupons);
//        for(int i=0;i<NUMCOUPONS;i++) {
//            System.out.println(coupons[i].toString());
//        }

        int result = getBestDiscount(coupons);
        System.out.print(result);
    }

    public static void addTOSet(int[] comb, HashSet<Integer> set) {
        for (int i = 0; i < comb.length; i++) {
            set.add(comb[i]);
        }
    }

    public static boolean isOverlapping(int[] Comb, HashSet<Integer> set) {
        for (int i = 0; i < Comb.length; i++) {
            if ((set.contains(Comb[i]))) {
                return true;
            }
        }
        return false;
    }

    public static int getBestDiscount(coupon[] coupons) {
        int bestDiscount = 0;
        for (int i = 0; i < coupons.length; i++) {
            int temp = coupons[i].val;
            HashSet<Integer> set = new HashSet<>();
            addTOSet(coupons[i].combination, set);
            for (int j = i + 1; j < coupons.length; j++) {
                if (!isOverlapping(coupons[j].combination, set)) {
                    addTOSet(coupons[j].combination, set);
                    temp += coupons[j].val;
                }
            }
            bestDiscount = Math.max(temp, bestDiscount);
        }
        return bestDiscount;
    }

}
