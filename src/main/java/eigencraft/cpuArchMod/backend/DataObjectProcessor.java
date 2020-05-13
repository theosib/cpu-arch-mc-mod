package eigencraft.cpuArchMod.backend;

/***All components of this mod should implement this to easily forward a dataObject***/
public interface DataObjectProcessor {
    public void processDataObject(AbstractDataObject dataObject);
}
