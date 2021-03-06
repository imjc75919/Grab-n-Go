package _04_shoppingCart.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import _05_orderProcess.model.OrderItemBean;

public class ShoppingCart {

	private Map<Integer, List<OrderItemBean>> shoppingCart = new HashMap<>();
	

	public Map<Integer, List<OrderItemBean>> getShoppingCart() {
		return shoppingCart;
	}
	
	

	public void setShoppingCart(Map<Integer, List<OrderItemBean>> shoppingCart) {
		this.shoppingCart = shoppingCart;
	}
	

	public ShoppingCart() {
	}
	

	public Map<Integer, List<OrderItemBean>> getContent() {
		return shoppingCart;
	}
	

	public void addToCart(int prod_Id, OrderItemBean odb) {

		if (shoppingCart.get(prod_Id) == null) {
			List<OrderItemBean> newList = new ArrayList<>();
			newList.add(odb);
			shoppingCart.put(prod_Id, newList);
		} else {
			List<OrderItemBean> oldList = shoppingCart.get(prod_Id);
			int[] result = isExist(odb, oldList);
			// result[0] : 0 == 不存在 ; 1== 已存在
			if (result[0] == 0) {
				oldList.add(odb);
				shoppingCart.put(prod_Id, oldList);
			} else {
				oldList.get(result[1]).setItem_amount
				(oldList.get(result[1]).getItem_amount() + odb.getItem_amount());
			}
		}
	}

	
	
	public int getItemNumber(){   // ShoppingCart.itemNumber
		int count =0;
		for( Integer proId : shoppingCart.keySet() ){
			List<OrderItemBean> list = shoppingCart.get(proId);
			for(int i=0;i<list.size();i++){
				count += list.get(i).getItem_amount();
			}
		}
		return count;
	}
	
	
	/* to print all the element of Map*/
	public void getListAllMapToPrint(ShoppingCart cart){   
		System.out.println("===================================");
		Map<Integer, List<OrderItemBean>> map = cart.getContent();
		for(Object proId : map.keySet()){
			System.out.println("proId = " + proId);
			List<OrderItemBean> list = (List<OrderItemBean>) map.get(proId);
			for(OrderItemBean oib :list){
				System.out.println(oib);
			}
		}
		System.out.println("===================================");
	}
	
	
	/* return a list of the element of Map*/
	public List<OrderItemBean> getListAllMap(){
		Map<Integer, List<OrderItemBean>> map = getContent();
		List<OrderItemBean> allList = new ArrayList<>();
		for(Object proId : map.keySet()){
//			System.out.println("proId = " + proId);
			List<OrderItemBean> list = (List<OrderItemBean>) map.get(proId);
			for(OrderItemBean oib :list){
				allList.add(oib);
//				System.out.println(oib);
			}
		}
		return allList;
	}
	

	public void getSubtotalToPrint(ShoppingCart cart){
		int subTotal = 0 ;
		Map<Integer, List<OrderItemBean>> map = getContent();
		List<OrderItemBean> allList = new ArrayList<>();
		for(Object proId : map.keySet()){
//			System.out.println("proId = " + proId);
			List<OrderItemBean> list = (List<OrderItemBean>) map.get(proId);
			for(OrderItemBean oib :list){
				subTotal += oib.getItem_amount()*oib.getItem_price();
			}
		}
//		return subTotal;
		System.out.println("subTotal = "+subTotal);
	}
	
	
	public int getSubtotal(){
		int subTotal = 0 ;
		Map<Integer, List<OrderItemBean>> map = getContent();
		List<OrderItemBean> allList = new ArrayList<>();
		for(Object proId : map.keySet()){
//			System.out.println("proId = " + proId);
			List<OrderItemBean> list = (List<OrderItemBean>) map.get(proId);
			for(OrderItemBean oib :list){
				subTotal += oib.getItem_amount()*oib.getItem_price();
			}
		}
		System.out.println("subTotal = "+subTotal);
		return subTotal;
	}

	/*
	public boolean modifyAmount(int prod_Id, OrderItemBean odb) {
		boolean result = false;
		if (shoppingCart.get(prod_Id) != null) {
			shoppingCart.put(prod_Id, odb);
			result = true;
		}
		return result;
	}
	*/
	

	public int modifyAmount(int prod_Id, String itemNote, int newAmount) {
		int ans = -1;
		List<OrderItemBean> oldList = shoppingCart.get(prod_Id);
		int[] result = findOIBPosition(itemNote, oldList);
		// result[0] : 0 == 不存在 ; 1== 已存在
		if (result[0] == 0) {
			System.out.println("品項不存在");
		} else {
			oldList.get(result[1]).setItem_amount(newAmount);
			ans = 1;
		}
		return ans;
	}
	
	
	public int modifyNote(int prod_Id, String oldItemNote, String newItemNote) {
		int ans = -1;
		List<OrderItemBean> oldList = shoppingCart.get(prod_Id);
		int[] result = findOIBPosition(oldItemNote, oldList);
		// result[0] : 0 == 不存在 ; 1== 已存在
		if (result[0] == 0) {
			System.out.println("品項不存在");
		} else {
			oldList.get(result[1]).setItem_note(newItemNote);
			ans = 1;
		}
		return ans;
	}
	 
	 
	public int deleteItem(int prod_Id, String itemNote) {
		int n = -1;
		List<OrderItemBean> oldList = shoppingCart.get(prod_Id);
		int[] result = findOIBPosition(itemNote, oldList);
		// result[0] : 0 == 不存在 ; 1== 已存在
		if (result[0] == 0) {
			System.out.println("品項不存在");
		} else {
			oldList.remove(result[1]);
			n = 1;
		}
		return n;
	}
	
	
	// 檢查新的品項是否已經在舊的品項存在了
	public int[] isExist(OrderItemBean newOid, List<OrderItemBean> oldList) {
		int[] result = new int[2];
		for (int i = 0; i < oldList.size(); i++) {
			if (oldList.get(i).getItem_note().equals(newOid.getItem_note())) {
				result[0] = 1;
				result[1] = i;
				break;
			} else {
				result[0] = 0;
			}
		}
		return result;
	}
	
	
	// 找尋有這個備註的餐點品項是在第幾個位置
	public int[] findOIBPosition(String ItemNote, List<OrderItemBean> oldList) {
		int[] result = new int[2];
		for (int i = 0; i < oldList.size(); i++) {
			if (oldList.get(i).getItem_note().equals(ItemNote)) {
				result[0] = 1;
				result[1] = i;
				break;
			} else {
				result[0] = 0;
			}
		}
		return result;
	}
	 

}
