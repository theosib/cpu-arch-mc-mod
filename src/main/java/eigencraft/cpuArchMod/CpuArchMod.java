package eigencraft.cpuArchMod;

import eigencraft.cpuArchMod.backend.DataObject;
import eigencraft.cpuArchMod.backend.DataObjectType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.util.NbtType;




public class CpuArchMod implements ModInitializer {

	

	@Override
	public void onInitialize() {
		//Todo
		System.out.println("Hello!");

		//Create ne dataObjectType
		DataObjectType testType = DataObjectType.create("test");
		//Add tag
		testType.addTag("testInt", NbtType.INT);

		//Create dataObject of testType from above
		DataObject o = new DataObject(testType);
		//Get type
		System.out.println(o.getType());
		//always with a value initialized, for easy checks, you never need to check if a tag exists on a dataObject if the dataObjectType declares this tag
		System.out.println(o.getInt("testInt"));
		//Set it to a value
		o.setInt("testInt",99);
		//Get the value
		System.out.println(o.getInt("testInt"));
	}
}
