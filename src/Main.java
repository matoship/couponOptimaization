import java.util.*;
/*
question:
Find the best combinations of the coupons for the shopping cart.
Restrictions:
cart size:99
coupons size:2000;

1. each coupon has a price barrier. For example, for a coupon {A,B,C,D} with price barrier of 20, it means a coupon may
only be used when the overall price of items A,,B,C,D is greater than the price barrier.
2. If an item is used in a coupon, any coupons that have the item included may not be used. For example, for coupon {A,B,C,D},
{B,C,D},{B,D}, if coupon {B,C,D} is used, any other coupon can not be used as they all have item B included.
 */


/*
bugs:
1.the algorithm always pick the first item of each coupon in every iteration. It ignores the possible combinations of
picking the second, third item as its choice.
2.the time complexity is around O(n^4) to O(n^3)
3.the algorithm can't print which coupons have been used for the best discount combination.

improving ideas:
implement DP to the algorithm.
*/
// driver
public class Main {
  public static int getRandomNumber(int max, int min) {
    Random random = new Random();
    return random.nextInt(max - min) + min;
  }

  public static int getRandomNumber(int max, int min, HashSet<Integer> set) {
    Random random = new Random();
    int tmp = random.nextInt(max - min) + min;

    if (set.add(tmp)) {
      return (tmp);
    }
    return getRandomNumber(max, min, set);
  }

  public static final int SHOPPINGCART = 99;
  public static final int NUMCOUPONS = 1000;
  public static final int NUMITEMS = 100000;
  public static final int DISCOUNT = 50;
  public static final int MAXPRICE = 100;
  public static final int MINPRICE = 10;
  public static final int MAXCOUNT = 5;
  public static final int THRESHOLD = 1000;
  public static List<coupon> couponList;

  public static void main(String[] args) {
    // set all items
    item[] allItems = new item[NUMITEMS];
    item[] items = new item[SHOPPINGCART];
    for (int i = 0; i < NUMITEMS; i++) {
      allItems[i] = new item(i, getRandomNumber(MAXPRICE, MINPRICE), getRandomNumber(MAXCOUNT, 1));
    }

    HashSet<Integer> nums = new HashSet<>();
    for (int i = 0; i < SHOPPINGCART; i++) {
      int tmp;
      tmp = getRandomNumber(NUMITEMS, 1, nums);
      items[i] = allItems[tmp];
    }
    nums.clear();

    // set coupons;
    coupon[] coupons = new coupon[NUMCOUPONS];
    for (int i = 0; i < NUMCOUPONS; i++) {
      List<item> combs = new ArrayList<>();
      for (int j = 0; j < getRandomNumber(NUMITEMS, 1); j++) {
        combs.add(allItems[getRandomNumber(NUMITEMS, 1, nums)]);
      }
      nums.clear();
      coupons[i] =
          new coupon(i, combs, getRandomNumber(DISCOUNT, 1), getRandomNumber(THRESHOLD, 100));
    }

    List<coupon> validCoupon = new ArrayList<>();
    couponRangeShrink(items, coupons, validCoupon);

    Collections.sort(validCoupon);
    for (coupon x : validCoupon) {
      Collections.sort(x.combination);
      System.out.println(x);
    }

    int result = getBestOffer(validCoupon);
    System.out.println(result);
  }

  public static boolean checkCoupon(List<coupon> validCoupon, HashSet<item> usedItem, int i) {
    int idx = 0;
    int sum = 0;
    List<Integer> temp = new ArrayList<>();
    // check if the current coupon has enough items that meet the price barrier.
    while (sum < validCoupon.get(i).threshold && idx < validCoupon.get(i).combination.size()) {
      if (!usedItem.contains(validCoupon.get(i).combination.get(idx))) {
        sum = sum + validCoupon.get(i).combination.get(idx).val;
        temp.add(idx);
      } else {
        return false;
      }
      idx++;
    }
    if (sum >= validCoupon.get(i).threshold) {
      for (Integer x : temp) {
        usedItem.add(validCoupon.get(i).combination.get(x));
      }
      return true;
    }
    return false;
  }

  public static void couponRangeShrink(item[] items, coupon[] coupons, List<coupon> validCoupon) {
    //reduce the size of the coupon list. Put valid coupons in a new list.
    for (coupon coupon : coupons) {
      List<item> newList = new ArrayList<>();
      coupon temp = new coupon();
      int sum = 0;
      for (item item : items) {
        if (coupon.checkValidity(item)) {
          sum = sum + item.val;
          newList.add(item);
        }
      }
      //if the overall value of the items of the cart that included in the coupon is greater
      // than the price barrier, put the coupon into the list. 
      if (newList.size() > 0 && sum >= coupon.threshold) {
        temp.cloneCoupon(coupon, newList);
        validCoupon.add(temp);
      }
    }
  }

  public static int getBestOffer(List<coupon> validCoupon) {
    int bestDiscount = 0;
    
    HashSet<item> usedItem = new HashSet<>();
    for (int i = 0; i < validCoupon.size(); i++) {
      int sum = 0;
      if (checkCoupon(validCoupon, usedItem, i)) {
        sum = validCoupon.get(i).discount;
      }

      for (int j = i + 1; j < validCoupon.size(); j++) {
        if (checkCoupon(validCoupon, usedItem, j)) {
          sum = sum + validCoupon.get(j).discount;
        }
      }
      bestDiscount = Math.max(sum, bestDiscount);
      usedItem.clear();
    }
    return bestDiscount;
  }
}
