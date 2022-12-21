package com.tims.intergration.utility;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tims.intergration.model.TimsInvoice;

import java.util.concurrent.TimeUnit;

/**
 * @author Joseph Kibe
 * Created on Wednesday, July 13, 2022
 * Time 10:07 PM
 */

public class InvoiceCache {

    private Cache<String, TimsInvoice> cache;

    public InvoiceCache() {
        this.cache = getCache();
    }

    private Cache<String, TimsInvoice>  getCache(){
         this.cache = CacheBuilder
                .newBuilder()
                .expireAfterWrite(35, TimeUnit.SECONDS).build();
        return cache;
    }


    public  void evictFromCache(TimsInvoice request){
        cache.invalidate(request.getTraderSystemInvoiceNumber());
    }

    public  void addToCache(TimsInvoice request){
        cache.put(request.getTraderSystemInvoiceNumber(), request);
    }

    public boolean checkIfPresent(String key){
        if (cache.getIfPresent(key) != null){
            return true;
        }
        return false;
    }

}
