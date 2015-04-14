package apriori;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by szeyiu on 10/1/14.
 * This class represents the element of frequent items.
 */
public class ItemSet {
    public List<Integer> itemList;//frequent items, sorted.
    public int occurrence=0;//times of occurrence
    public ItemSet(){
        itemList=new ArrayList<Integer>();
        occurrence=0;
    }
    public ItemSet(List<Integer> itemList){
        this.itemList=new ArrayList<Integer>(itemList.size());
        for(int item:itemList)
            this.itemList.add(item);
        occurrence=0;
    }
    public ItemSet(int... items){
        itemList  = new ArrayList<Integer>(items.length);
        for(int i=0;i<items.length;++i)
            itemList.add(items[i]);
        occurrence=0;
    }
    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()){
            return false;
        }
        ItemSet ig = (ItemSet) obj;
        return itemList.equals(ig.itemList);
    }
    @Override
    public int hashCode(){
        return itemList.hashCode();
    }
}