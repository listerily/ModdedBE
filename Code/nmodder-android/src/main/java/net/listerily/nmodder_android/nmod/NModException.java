package net.listerily.nmodder_android.nmod;

import androidx.annotation.Nullable;

public class NModException extends Exception {

    public NModException(String message)
    {
        super(message);
    }

    public NModException(String message,Error cause)
    {
        super(message,cause);
    }
}
