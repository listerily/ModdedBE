package net.listerily.endercore.android.exception;

public class NModWarning extends Exception{
    public NModWarning(String message)
    {
        super(message);
    }

    public NModWarning(String message,Error cause)
    {
        super(message,cause);
    }
}
