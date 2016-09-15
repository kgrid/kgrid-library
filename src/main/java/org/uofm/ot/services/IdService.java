package org.uofm.ot.services;

/**
 * Created by pboisver on 9/15/16.
 */
public class IdService {

    static int id = 0;
    static String naan = "12345";

    public String mint() {
        String pid = String.valueOf(id++);
        return "ark:/" + naan + "/" + pid;
    }
}
