package com.gmail.uia059466.liska.catalog

import com.gmail.uia059466.liska.data.database.CatalogDatabase
import com.gmail.uia059466.liska.utils.ItemFormatter.Companion.createStringItem

class CatalogSelectState {

    val selected= mutableListOf<Int>()
    var quantityMap : HashMap<Int, Int> = HashMap()
    val folders = mutableListOf<fufu.Catalog>()
    val items = mutableListOf<fufu.Item>()
    val commandAdd = mutableListOf<AddItemsInCatalog>()
    var counter=0

    fun start(raw: List<CatalogDatabase>)
    {
        for (r in raw){
            folders.add(fufu.Catalog(id = r.id, idFufu = ++counter, title = r.title))
            items.addAll(r.list.map { fufu.Item(id = r.id,idFufu = ++counter, title = it.title,
                unit = it.unit,
                quantity = it.quantity,
                isNew = false,) })
        }
    }


    fun selected(idFufu: Int){
        selected.add(idFufu)
    }

    fun unSelected(idFufu: Int){
        selected.remove(idFufu)
    }

    fun getItems(id: Long):List<fufu.Item> {
        return  items.filter { it.id==id}

    }

    fun addNewSearch(id: Long,title: String)
    {
        items.add(fufu.Item(id = id, idFufu = ++counter, title = title, isNew = false,
            unit = "",
            quantity = 0
        ))
        commandAdd.add(AddItemsInCatalog(id = id, title = title))
    }

    fun getSearchResult(str: String): List<fufu.Item> {
        val result= mutableListOf<fufu.Item>()
        if (str.isNotBlank()){
            result.add(fufu.Item(id = 0, idFufu = ++counter, title = str, isNew = true,
                unit = "",
                quantity = 0
            ))
        }
        val search=items.filter { it.title.contains(str) }
        result.addAll(search)
        return result
    }

    fun requestSelectedForAdding():List<String> {

        return if (quantityMap.isNotEmpty()){
            requestForQantity()
        }else{
            requestWithSelected()
        }
    }

    private fun requestWithSelected(): List<String> {
        val result= mutableListOf<String>()
        for (s in selected){
            val finded =items.find { it.idFufu==s }
            if (finded!=null){
                val str= if (finded.unit.isBlank()){
                    finded.title
                } else{
                    "${finded.title}, ${finded.unit}"
                }

                result.add(str)
            }
        }
        return result
    }

    private fun requestForQantity(): List<String> {
        val result= mutableListOf<String>()
        for (item in quantityMap){
            if (item.value!=0){
                val finded =items.find { it.idFufu==item.key }

                if (finded!=null){
                    val str=createStringItem(title =finded.title,quantity=item.value,unit = finded.unit)
                    result.add(str)
                }

            }
        }

        for (s in selected){
            val finded =items.find { it.idFufu==s }
            if (finded!=null){
                val str= createStringItem(title =finded.title,quantity=null,unit = finded.unit)
                result.add(str)
            }
        }
        return result
    }
// todo test



    fun isSelected(idFufu: Int): Boolean {
        return selected.contains(idFufu)
    }

    fun quantity(idFufu: Int):Int{
            return quantityMap.get(idFufu)?:0
    }

    fun increase(idFufu: Int,itemQByDefault:Int):Int{
        val old=quantityMap.get(idFufu)
        return if (old!=null){
            val newValue=old+1
            quantityMap[idFufu] = newValue
            newValue
        }else if(itemQByDefault>0){
            quantityMap.put(idFufu,itemQByDefault)
            itemQByDefault
        } else{
            quantityMap.put(idFufu,1)
            1
        }
   }


    fun decrease(idFufu: Int):Int{
        val old=quantityMap.get(idFufu)
        return if (old!=null){
            val newValue= if (old-1<0) 0 else old-1
            quantityMap[idFufu] = newValue
            newValue
        }else{
            quantityMap.put(idFufu,0)
            0
        }
    }

}

sealed class fufu {
    class Catalog(
        val id: Long,
        val idFufu: Int,
        val title: String
    ):fufu()

    class Item(
        val id: Long,
        val idFufu:Int,
        val title: String,
        val unit:String,
        val quantity:Int,
        var isNew:Boolean=false
    ):fufu()

}

class AddItemsInCatalog(val id: Long,
val title: String)