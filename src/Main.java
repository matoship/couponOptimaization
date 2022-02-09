import java.util.*;

public class Main {
  static class Coupon {
    public String cTag;
    public Set<String> iTags;
    public int minPrice;
    public int cut;

    public Coupon(
            String cTag,
            Set<String> iTags,
            int minPrice,
            int cut)
    {
      this.cTag = cTag;
      this.iTags = iTags;
      this.minPrice = minPrice;
      this.cut = cut;
    }
  }

  static class CouponCombo {
    public Set<String> cTags;
    public int cut;

    @Override
    public String toString() {
      return "CouponCombo{" +
              "cTags=" + cTags +
              ", cut=" + cut +
              '}';
    }

    public CouponCombo(
            Set<String> cTags,
            int cut)
    {
      this.cTags = cTags;
      this.cut = cut;
    }
  }

  static class CartItem {
    public String iTag;
    public int number;
    public int price;

    public CartItem(
            String iTag,
            int number,
            int price)
    {
      this.iTag = iTag;
      this.number = number;
      this.price = price;
    }
  }

  public static Set<Set<String>> getAvailableCouponItems(
          Coupon coupon,
          Map<String, CartItem> cartItems,
          Set<String> leftOverITags)
  {
    int minPrice = coupon.minPrice;
    Set<String> currentItags = new HashSet<String>();
    Set<String> availableITags = new HashSet<String>(coupon.iTags);
    Set<Set<String>> result = new HashSet<Set<String>>();
    getAvailableCouponItemsJob(0, cartItems, leftOverITags, availableITags, currentItags, result, minPrice);
    return result;
  }

  public static void getAvailableCouponItemsJob(
          int price,
          Map<String, CartItem> cartItems,
          Set<String> leftOverITags,
          Set<String> availableITags,
          Set<String> currentItags,
          Set<Set<String>> result,
          int minPrice)
  {
    for (String iTag : availableITags) {
      if (leftOverITags.contains(iTag)) {
        CartItem item = cartItems.get(iTag);
        int iPrice = item.price * item.number;
        int newPrice = price + iPrice;
        Set<String> newITags = new HashSet<String>(currentItags);
        String newITagsStr = newITags.toString();
        newITags.add(iTag);
        if (newPrice >= minPrice) {
          result.add(newITags);
        } else {
          Set<String> newAvailableITags = new HashSet<String>(availableITags);
          newAvailableITags.remove(iTag);
          getAvailableCouponItemsJob(
                  newPrice,
                  cartItems,
                  leftOverITags,
                  newAvailableITags,
                  newITags,
                  result,
                  minPrice);
        }
      }
    }
  }

  public static CouponCombo computeCouponCombo(
          Map<String, Coupon> coupons,
          Map<String, CartItem> items)
  {
    Map<String, CouponCombo> mem = new HashMap<String, CouponCombo>();
    CouponCombo bestCombo = new CouponCombo(new HashSet<String>(), 0);
    CouponCombo currentCombo = new CouponCombo(new HashSet<String>(), 0);
    Set<String> removedITags = new HashSet<String>();
    Set<String> leftOverITags = new HashSet<String>(items.keySet());
    computeCouponComboJob(
            coupons,
            items,
            mem,
            currentCombo,
            bestCombo,
            removedITags,
            leftOverITags);

    return bestCombo;
  }

  public static void computeCouponComboJob(
          Map<String, Coupon> coupons,
          Map<String, CartItem> items,
          Map<String, CouponCombo> mem,
          CouponCombo prevCombo,
          CouponCombo bestCombo,
          Set<String> removedITags,
          Set<String> leftOverITags)
  {
    if (leftOverITags.isEmpty()) {
      return;
    }

    for (String cTag : coupons.keySet()) {
      System.out.println("Checking coupon " + cTag + " on items " + leftOverITags.toString());
      Coupon coupon = coupons.get(cTag);
      Set<String> currentCTags = new HashSet<String>(prevCombo.cTags);
      currentCTags.add(cTag);
      int currentCut = prevCombo.cut + coupon.cut;
      System.out.println("    Getting coupon " + coupon.cTag + " items for items: " + leftOverITags.toString());
      Set<Set<String>> removedItemCombination = getAvailableCouponItems(coupon, items, leftOverITags);
      System.out.println("    Result is: " + removedItemCombination.toString());
      Map<String, Coupon> newCoupons = new HashMap<String, Coupon>(coupons);
      newCoupons.remove(cTag);
      System.out.println("    new Coupons: " + newCoupons.toString());
      String removeCombi = removedItemCombination.toString();
      for (Set<String> iTags : removedItemCombination) {
        System.out.println("    Try remove items: " + iTags.toString());
        Set<String> newLeftOverITags = new HashSet<String>(leftOverITags);
        newLeftOverITags.removeAll(iTags);
        Set<String> totalRemovedITags = new HashSet<String>();
        totalRemovedITags.addAll(removedITags);
        totalRemovedITags.addAll(iTags);
        String key = totalRemovedITags.toString();

        if (mem.containsKey(key)) {
          CouponCombo a = mem.get(key);
          String b = mem.get(key).toString();
          System.out.println("        Found collision: " + key + " with combo: " + mem.get(key).toString());
        }

        if (mem.containsKey(key) && mem.get(key).cut >= currentCut) {
          System.out.println("        Colide, skip.");
          return;
        }

        CouponCombo currentCombo = new CouponCombo(currentCTags, currentCut);
        System.out.println("        Add combo key: " + key + " value: " + currentCombo.toString() + " to mem.");
        mem.put(key, currentCombo);

        if (bestCombo.cut < currentCombo.cut) {
          bestCombo.cTags = currentCombo.cTags;
          bestCombo.cut = currentCombo.cut;
        }

        System.out.println("        Proceed to next.");
        computeCouponComboJob(newCoupons, items, mem, currentCombo, bestCombo, totalRemovedITags, newLeftOverITags);
      }
    }
  }
  public static void main(String[] args ){
    CartItem item1 = new CartItem("A",1,3);
    CartItem item2 = new CartItem("B",3,4);
    CartItem item3 = new CartItem("C",2,5);
    HashSet<String> h1 = new HashSet<>();
    h1.add("A");
    h1.add("B");
    h1.add("C");
    HashSet<String> h2 = new HashSet<>();
    h2.add("A");
    h2.add("B");

    HashSet<String> h3 = new HashSet<>();
    h3.add("B");

    Coupon c1 = new Coupon("abc",h1,4,3);
    Coupon c2 = new Coupon("ab",h2,2,5);
    Coupon c3 = new Coupon("b",h3,1,2);

    Map<String, Coupon> coupons = new HashMap<>();
    coupons.put("abc",c1);
    coupons.put("ab",c2);
    coupons.put("b",c3);

    Map<String, CartItem> items = new HashMap<>();
    items.put("A",item1);
    items.put("B",item2);
    items.put("C",item3);

    System.out.println(computeCouponCombo(coupons,items));

  }
}