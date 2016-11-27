package com.dayuan.dy_6260chartscanner.util;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.util.Log;

public class WifiAdmin {

	/** * ����һ��WifiManager����,�ṩWifi����ĸ�����ҪAPI����Ҫ����wifi��ɨ�衢�������ӡ�������Ϣ�� */
    private WifiManager mWifiManager;
    // WIFIConfiguration����WIFI��������Ϣ������SSID��SSID���ء�password�ȵ�����
    private List<WifiConfiguration> wifiConfigList;
    // ����һ��WifiInfo����
    private WifiInfo mWifiInfo;
    // ɨ��������������б�
    private List<ScanResult> mWifiList;
 // ���������б�
    private List<WifiConfiguration> mWifiConfigurations;

    WifiLock mWifiLock;
    public static final int TYPE_NO_PASSWD = 0x11;
    public static final int TYPE_WEP = 0x12;
    public static final int TYPE_WPA = 0x13;

    public WifiAdmin(Context context) {
        // ���WifiManager����
        mWifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);

        // ȡ��WifiInfo����
        mWifiInfo = mWifiManager.getConnectionInfo();
    }
    /** * ��wifi */
    public void openWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
    }
    /** * �ر�wifi */
    public void closeWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }
    /** * ��鵱ǰwifi״̬ * * @return */
    public int checkState() {
        return mWifiManager.getWifiState();
    }
    /** * ����wifiLock */
    public void acquireWifiLock() {
        mWifiLock.acquire();
    }
    /** * ����wifiLock */
    public void releaseWifiLock() {
        // �ж��Ƿ�����
        if (mWifiLock.isHeld()) {
            mWifiLock.acquire();
        }
    }
    /** * ����һ��wifiLock */
    public void createWifiLock() {
        mWifiLock = mWifiManager.createWifiLock("test");
    }
    /** * �õ����úõ����� * * @return */
    public List<WifiConfiguration> getWiFiConfiguration() {
        return mWifiConfigurations;
    }
    /** * ָ�����úõ������������ * * @param index */
    public void connetionConfiguration(int index) {
        if (index > mWifiConfigurations.size()) {
            return;
        }
        // �������ú�ָ��ID������
        mWifiManager.enableNetwork(mWifiConfigurations.get(index).networkId,
                true);
    }
    public void startScan() {
        mWifiManager.startScan();
        // �õ�ɨ����
        mWifiList = mWifiManager.getScanResults();
        // �õ����úõ���������
        mWifiConfigurations = mWifiManager.getConfiguredNetworks();
    }

    /** * �õ������б� * * @return */
    public List<ScanResult> getWifiList() {
        return mWifiList;
    }
    /** * �鿴ɨ���� * * @return */
    public StringBuffer lookUpScan() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mWifiList.size(); i++) {
            sb.append("Index_" + new Integer(i + 1).toString() + ":");
            // ��ScanResult��Ϣת����һ���ַ�����
            // ���аѰ�����BSSID��SSID��capabilities��frequency��level
            sb.append((mWifiList.get(i)).toString()).append("\n");
        }
        return sb;
    }

    public String getMacAddress() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }

    public String getBSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }

    public int getIpAddress() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }

    /** * �õ����ӵ�ID * * @return */
    public int getNetWordId() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }

    /** * �õ�wifiInfo��������Ϣ * * @return */
    public String getWifiInfo() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
    }

    /** * ���һ�����粢���� * * @param configuration */
    public void addNetwork(WifiConfiguration configuration) {
        int wcgId = mWifiManager.addNetwork(configuration);
        mWifiManager.enableNetwork(wcgId, true);
    }

    public WifiConfiguration createWifiInfo(String SSID, String Password,
            int Type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        WifiConfiguration tempConfig = this.isExsits(SSID);
        if (tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }

        if (Type == 1) // WIFICIPHER_NOPASS
        {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (Type == 2) // WIFICIPHER_WEP
        {
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + Password + "\"";
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (Type == 3) // WIFICIPHER_WPA
        {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }

    private WifiConfiguration isExsits(String ssid) {
        WifiConfiguration config = new WifiConfiguration();

        if (mWifiList.size() > 0) {
            for (int i = 0; i < mWifiList.size(); i++) {
                if (mWifiList.get(i).SSID.equals(ssid)) {

                    config = mWifiConfigurations.get(i);
                }
            }
        }
        return config;
    }

    /** * �Ͽ�ָ��ID������ * * @param netId */
    public void disConnectionWifi(int netId) {
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }

    // ============
    /** * ����ָ��Id��WIFI * * @param wifiId * @return */
    public boolean ConnectWifi(int wifiId) {
        for (int i = 0; i < wifiConfigList.size(); i++) {
            WifiConfiguration wifi = wifiConfigList.get(i);
            if (wifi.networkId == wifiId) {
                while (!(mWifiManager.enableNetwork(wifiId, true))) {// �����Id����������
                    Log.i("ConnectWifi",
                            String.valueOf(wifiConfigList.get(wifiId).status));// status:0--�Ѿ����ӣ�1--�������ӣ�2--��������
                }
                return true;
            }
        }
        return false;
    }

    /** * �õ�Wifi���úõ���Ϣ */
    public void getConfiguration() {
        wifiConfigList = mWifiManager.getConfiguredNetworks();// �õ����úõ�������Ϣ
        for (int i = 0; i < wifiConfigList.size(); i++) {
            Log.i("getConfiguration", wifiConfigList.get(i).SSID);
            Log.i("getConfiguration",
                    String.valueOf(wifiConfigList.get(i).networkId));
        }
    }

    /** * �ж�ָ��WIFI�Ƿ��Ѿ����ú�,����WIFI�ĵ�ַBSSID,����NetId * * @param SSID * @return */
    public int IsConfiguration(String SSID) {
        Log.i("IsConfiguration", String.valueOf(wifiConfigList.size()));
        for (int i = 0; i < wifiConfigList.size(); i++) {
            Log.i(wifiConfigList.get(i).SSID,
                    String.valueOf(wifiConfigList.get(i).networkId));
            if (wifiConfigList.get(i).SSID.equals(SSID)) {// ��ַ��ͬ
                return wifiConfigList.get(i).networkId;
            }
        }
        return -1;
    }

    /** * ���ָ��WIFI��������Ϣ,ԭ�б����ڴ�SSID * * @param wifiList * @param ssid * @param pwd * @return */
    public int AddWifiConfig(List<ScanResult> wifiList, String ssid, String pwd) {
        int wifiId = -1;
        for (int i = 0; i < wifiList.size(); i++) {
            ScanResult wifi = wifiList.get(i);
            if (wifi.SSID.equals(ssid)) {
                Log.i("AddWifiConfig", "equals");
                WifiConfiguration wifiCong = new WifiConfiguration();
                wifiCong.SSID = "\"" + wifi.SSID + "\"";// \"ת���ַ�������"
                wifiCong.preSharedKey = "\"" + pwd + "\"";// WPA-PSK����
                wifiCong.hiddenSSID = false;
                wifiCong.status = WifiConfiguration.Status.ENABLED;
                wifiId = mWifiManager.addNetwork(wifiCong);// �����úõ��ض�WIFI������Ϣ���,�����ɺ�Ĭ���ǲ�����״̬���ɹ�����ID������Ϊ-1
                if (wifiId != -1) {
                    return wifiId;
                }
            }
        }
        return wifiId;
    }
}
