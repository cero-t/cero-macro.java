package ninja.cero.macro.vjoymacro.vjoy;

public interface VjoyInterface {
    // may not be proper args
    boolean vJoyEnabled();

    boolean AcquireVJD(int id);

    int GetVJDStatus(int id);

    int GetVJDButtonNumber(int id);

    int GetVJDContPovNumber(int id);

    int GetVJDDiscPovNumber(int id);

    boolean SetBtn(int sw, int id, int btnId);

    int ResetAll();

    int ResetVJD(int id);

    int GetVJDAxisMax(int id, int usage, int maxAxis);

    int GetVJDAxisExist(int id, int usage);

    boolean SetAxis(int value, int id, int usage);

    int RelinquishVJD(int id);

    int SetContPov(int value, int id, int nPov);

    int SetDiscPov(int value, int id, int nPov);
}
