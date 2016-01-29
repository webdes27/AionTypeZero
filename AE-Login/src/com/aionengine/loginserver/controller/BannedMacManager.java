package com.aionengine.loginserver.controller;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionengine.loginserver.dao.BannedMacDAO;
import com.aionengine.loginserver.model.base.BannedMacEntry;
import javolution.util.FastMap;

import java.sql.Timestamp;
import java.util.Map;

/**
 * @author KID
 */
public class BannedMacManager {
    private static BannedMacManager manager = new BannedMacManager();

    private Map<String, BannedMacEntry> bannedList = new FastMap<String, BannedMacEntry>();

    public static BannedMacManager getInstance() {
        return manager;
    }

    private BannedMacDAO dao = DAOManager.getDAO(BannedMacDAO.class);

    public BannedMacManager() {
        bannedList = dao.load();
    }

    public void unban(String address, String details) {
        if (bannedList.containsKey(address)) {
            bannedList.remove(address);
            dao.remove(address);
        }
    }

    public void ban(String address, long time, String details) {
        BannedMacEntry mac = new BannedMacEntry(address, new Timestamp(time), details);
        this.bannedList.put(address, mac);
        this.dao.update(mac);
    }

    public final Map<String, BannedMacEntry> getMap() {
        return this.bannedList;
    }
}