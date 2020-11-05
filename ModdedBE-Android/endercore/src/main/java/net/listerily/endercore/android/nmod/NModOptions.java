package net.listerily.endercore.android.nmod;

import com.google.gson.Gson;

public class NModOptions {

    private OptionsData data;
    public NModOptions() {
        data = new OptionsData();
    }

    public NModOptions(String jsonContent) {
        data = new Gson().fromJson(jsonContent,OptionsData.class);
    }

    public String toJsonContent(){
        return new Gson().toJson(data);
    }

    public boolean fromJsonContent(String jsonContent)
    {
        data = new Gson().fromJson(jsonContent,OptionsData.class);
        if(data == null)
        {
            data = new OptionsData();
            return false;
        }
        return true;
    }

    public DataElement[] getInstalledNModElements() {
        return data.installed_nmods;
    }

    public void setInstalledNModElements(DataElement[] elements){
        data.installed_nmods = elements;
    }

    public void addNewInstalledNModElement(String uuid) {
        DataElement[] newArray = new DataElement[data.installed_nmods.length + 1];
        System.arraycopy(data.installed_nmods, 0, newArray, 0, data.installed_nmods.length);
        newArray[data.installed_nmods.length] = new DataElement();
        newArray[data.installed_nmods.length].uuid = uuid;
        newArray[data.installed_nmods.length].enabled = true;
        data.installed_nmods = newArray;
    }

    public boolean getNModAvailability(String uuid)
    {
        for(int i = 0;i < data.installed_nmods.length;++i)
            if (data.installed_nmods[i].uuid.equals(uuid))
                return data.installed_nmods[i].enabled;
        return false;
    }

    public void setNModAvailability(String uuid,boolean availability)
    {
        for(int i = 0;i < data.installed_nmods.length;++i)
            if (data.installed_nmods[i].uuid.equals(uuid))
                data.installed_nmods[i].enabled = availability;
    }

    public void setNModDisabled(String uuid)
    {
        setNModAvailability(uuid,false);
    }

    public void setNModEnabled(String uuid)
    {
        setNModAvailability(uuid,true);
    }

    public boolean findIfExists(String uuid){
        for(int i = 0;i < data.installed_nmods.length;++i)
            if (data.installed_nmods[i].uuid.equals(uuid))
                return true;
        return false;
    }

    public boolean removeInstalledNModElement(String uuid) {
        if(!findIfExists(uuid))
            return false;
        DataElement[] newArray = new DataElement[data.installed_nmods.length - 1];
        for(int i = 0,j = 0;i < data.installed_nmods.length;++i){
            DataElement element = data.installed_nmods[i];
            if(!element.uuid.equals(uuid))
                newArray[j++] = element;
        }
        data.installed_nmods = newArray;
        return true;
    }

    public static class DataElement {
        String uuid = null;
        boolean enabled = true;
    }
    private static class OptionsData {
        DataElement[] installed_nmods;
    }
}
