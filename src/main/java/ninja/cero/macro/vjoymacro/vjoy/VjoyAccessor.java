package ninja.cero.macro.vjoymacro.vjoy;

import java.io.Closeable;

public class VjoyAccessor implements AutoCloseable, Closeable {
    static final int VJD_STAT_OWN = 0;
    static final int VJD_STAT_FREE = 1;
    static final int VJD_STAT_BUSY = 2;
    static final int VJD_STAT_MISS = 3;
    static final int VJD_STAT_UNKN = 4;

    static final int HID_USAGE_X = 0x30;
    static final int HID_USAGE_Y = 0x31;
    static final int HID_USAGE_Z = 0x32;
    static final int HID_USAGE_RX = 0x33;
    static final int HID_USAGE_RY = 0x34;
    static final int HID_USAGE_RZ = 0x35;
    static final int HID_USAGE_SL0 = 0x36;
    static final int HID_USAGE_SL1 = 0x37;
    static final int HID_USAGE_WHL = 0x38;
    static final int HID_USAGE_POV = 0x39;

    int vjoyId;
    VjoyInterface vjoyInterface;

    public VjoyAccessor(VjoyInterface vjoyInterface, int vjoyId) {
        this.vjoyId = vjoyId;
        this.vjoyInterface = vjoyInterface;
    }

    public void init() throws InterruptedException {
        int status = vjoyInterface.GetVJDStatus(vjoyId);

        if (status == VJD_STAT_FREE) {
            System.out.println("device [" + vjoyId + "] is free");
        } else if (status == VJD_STAT_OWN) {
            throw new RuntimeException("device [" + vjoyId + "] is already owned by this feeder");
        } else if (status == VJD_STAT_BUSY) {
            throw new RuntimeException("device [" + vjoyId + "] is already owned by another feeder");
        } else if (status == VJD_STAT_MISS) {
            throw new RuntimeException("device [" + vjoyId + "] is not installed or disabled");
        } else {
            throw new RuntimeException("unknows status - " + status);
        }

        boolean jvd = vjoyInterface.AcquireVJD(vjoyId);
        if (jvd) {
            System.out.println("device [" + vjoyId + "] acquired");
        } else {
            throw new RuntimeException("device [" + vjoyId + "] could not be acuired");
        }

        Thread.sleep(50);
        vjoyInterface.ResetVJD(vjoyId);
    }

    public boolean push(String key) {
        switch (key) {
            case "lp":
                return push(4);
            case "mp":
                return push(1);
            case "hp":
                return push(6);
            case "lk":
                return push(3);
            case "mk":
                return push(2);
            case "hk":
                return push(8);
            case "1":
                return axis(0, 32767);
            case "2":
                return axis(16384, 32767);
            case "3":
                return axis(32767, 32767);
            case "4":
                return axis(0, 16384);
            case "5":
                return axis(16384, 16384);
            case "6":
                return axis(32767, 16384);
            case "7":
                return axis(0, 0);
            case "8":
                return axis(16384, 0);
            case "9":
                return axis(32767, 0);
            default:
                throw new RuntimeException("Unknown command : " + key);
        }
    }

    public boolean release(String key) {
        switch (key) {
            case "lp":
                return release(4);
            case "mp":
                return release(1);
            case "hp":
                return release(6);
            case "lk":
                return release(3);
            case "mk":
                return release(2);
            case "hk":
                return release(8);
            default:
                throw new RuntimeException("Unknown command : " + key);
        }
    }


    public boolean axis(int x, int y) {
        return vjoyInterface.SetAxis(x, vjoyId, HID_USAGE_X) && vjoyInterface.SetAxis(y, vjoyId, HID_USAGE_Y);
    }

    public boolean push(int id) {
        return vjoyInterface.SetBtn(1, this.vjoyId, id);
    }

    public boolean release(int id) {
        return vjoyInterface.SetBtn(0, this.vjoyId, id);
    }

    @Override
    public void close() {
        vjoyInterface.ResetAll();
        vjoyInterface.RelinquishVJD(vjoyId);
    }
}
