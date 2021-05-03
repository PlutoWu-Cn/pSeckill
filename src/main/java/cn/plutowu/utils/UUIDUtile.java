package cn.plutowu.utils;

import java.util.UUID;

/**
 * @author PlutoWu
 * @date 2021/05/01
 */
public class UUIDUtile {
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
