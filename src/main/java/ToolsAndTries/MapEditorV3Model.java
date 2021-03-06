package ToolsAndTries;

import javax.swing.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Map;

public class MapEditorV3Model extends DefaultListModel {
    private final XMLDOMWriter writer = new XMLDOMWriter();
    private final Document board= writer.createEmptyBoard();
    private final Element root = board.createElement("board");
    private JList<String> view;
    private JList<String> subView;
    private Map<String,DefaultListModel<String>> subModels = new HashMap<>();
    private MapEditorV3 frame;

    public MapEditorV3Model(MapEditorV3 frame)
    {
        this.frame = frame;
        board.appendChild(root);
    }

    public void addContinent(String name)
    {
        Element continent = board.createElement(name);
        root.appendChild(continent);
        addElement(name);
        subModels.put(name,new DefaultListModel<>());
    }

    public void generateBoard(){
        writer.generateMapWithDoc(board);

    }

    public void addTerritory(int x, int y) {
        String continent = view.getSelectedValue();
        if (continent==null){
            System.out.println("No Continent Selected");
            return;
        }
        String name = JOptionPane.showInputDialog("Territory Name");
        if (name==null){return;}
        Element territory = board.createElement(name);
        territory.setAttribute("x",String.valueOf(x));
        territory.setAttribute("y",String.valueOf(y));
        Element p = (Element)board.getElementsByTagName(continent).item(0);
        p.appendChild(territory);
        frame.addButton(name,x,y);

        DefaultListModel<String> subViewModel = subModels.get(continent);
        subView.setModel(subViewModel);
        subViewModel.addElement(name);

    }

    public JList<String> getView() {
        return view;
    }

    public void setView(JList<String> view) {
        this.view = view;
    }

    public JList<String> getSubView() {
        return subView;
    }

    public void setSubView(JList<String> subView) {
        this.subView = subView;
    }

    public void removeTerritory() {
        String s = subView.getSelectedValue();
        if(s==null){return;}
        DefaultListModel<String> temp = (DefaultListModel)subView.getModel();
        temp.removeElement(s);
        frame.removeButton(s);
        Node node = board.getElementsByTagName(s).item(0);
        node.getParentNode().removeChild(node);
    }

    public void changeSubModel() {
        String continent = view.getSelectedValue();
        if (continent==null){
            System.out.println("No Continent Selected");
            return;
        }

        DefaultListModel<String> subViewModel = subModels.get(continent);
        subView.setModel(subViewModel);
    }
}
