package jo.vpl.hub.list;

import java.util.ArrayList;
import java.util.List;
import jo.vpl.core.Hub;
import jo.vpl.core.VPLControl;
import javafx.scene.control.Label;
import jo.vpl.core.HubInfo;

/**
 *
 * @author JoostMeulenkamp
 */
@HubInfo(
        name = "List.AddItemToEnd",
        category = "List",
        description = "Add an item to the end of a list",
        tags = {"list", "add"})
public class AddItemToEnd extends Hub {

    public AddItemToEnd(VPLControl hostCanvas) {
        super(hostCanvas);

        setName("+");

        //There is no checking of list in port make connection boolean statement
        //Might want to fix that!
        addInPortToHub("list", Object.class);
        addInPortToHub("obj", Object.class);

        addOutPortToHub("obj", Object.class);

        Label label = new Label("...+");
        label.getStyleClass().add("hub-text");
        
        addControlToHub(label);
    }

    /**
     * calculate function is called whenever new data is incoming
     */
    @Override
    public void calculate() {
        //Get incoming data
        Object raw = inPorts.get(0).getData();
        Object item = inPorts.get(1).getData();

        //Finish calculate if there is no incoming data
        if (raw == null || item == null) {
            outPorts.get(0).dataType = Object.class;
            outPorts.get(0).name = "obj";
            return;
        }

        //Check if all incoming data is in the correct format
        boolean hasError = false;
        if (!(raw instanceof List)) {
            System.out.println("This is not a list");
            hasError = true;
        }
        if (item instanceof List) {
            System.out.println("Just one item can be added at a time");
            hasError = true;
        }
        if (inPorts.get(1).connectedConnections.size() > 0
                && inPorts.get(0).connectedConnections.size() > 0) {
            if (inPorts.get(0).connectedConnections.get(0).getStartPort().dataType
                    != inPorts.get(1).connectedConnections.get(0).getStartPort().dataType) {
                System.out.println("Element is not of same type as the list's");
                hasError = true;
            }
        }
        if (hasError) {
            return;
        }

        //Add item to end
        List source = (List) raw;

        List target = new ArrayList();
        target.addAll(source);
        target.add(item);

        //Set outgoing data
        outPorts.get(0).setData(target);

        //Set data type corresponding to source
        outPorts.get(0).dataType = inPorts.get(0).connectedConnections.get(0).getStartPort().dataType;
        outPorts.get(0).name = inPorts.get(0).connectedConnections.get(0).getStartPort().name;
    }

    @Override
    public Hub clone() {
        AddItemToEnd hub = new AddItemToEnd(hostCanvas);
        //Specify further copy statements here
        return hub;
    }
}