package RISK;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class XMLHandler extends DefaultHandler {
    private boolean isPlayerName = false;
    private boolean isTerritoryName = false;
    private boolean isHolder = false;
    private boolean isPlayerTroops = false;
    private boolean isTerritoryTroops = false;
    private boolean isStage = false;
    private boolean isID = false;
    private boolean isCurrentPlayer = false;
    private boolean isContinent = false;
    private boolean isStatusLabel = false;
    private boolean isOriginTerritoryButtonClick = false;
    private boolean isTargetTerritoryButtonClick = false;
    private boolean isOriginTerritoryName = false;
    private boolean isTargetTerritoryName = false;

    private RiskModel model;
    private Player player;
    private Territory territory;
    private Continent tempContinent;
    private ArrayList<Player> importPlayers;
    private String statusLabel;
    private String originTerritoryButtonClick;
    private String targetTerritoryButtonClick;
    private String originTerritoryName;
    private String targetTerritoryName;
    private String mapName;


    public XMLHandler(String mapName) throws Exception {
        this.mapName = mapName;
        importPlayers = new ArrayList<>();
    }

    /**Set the models
     * @param model the model to set
     */
    public void setModel(RiskModel model) {
        this.model = model;
    }

    public void toXMLFile(String filename) throws SAXException {
        SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();

        try {
            TransformerHandler handler = factory.newTransformerHandler();
            Transformer transformer = handler.getTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            File file = new File(filename+".xml");
            Result result = new StreamResult(file);
            handler.setResult(result);

            AttributesImpl attr = new AttributesImpl();

            handler.startDocument();
            attr.clear();
            handler.startElement("", "", "RISK", attr);


            //store player in XML
            for (int i = 0; i < model.getNumberPlayers(); i++) {
                attr.addAttribute("", "", "ID", "", String.valueOf(i));
                if(model.getPlayerById(i) instanceof AIPlayer){
                    handler.startElement("", "", "AIPlayer", attr);
                }else {
                    handler.startElement("", "", "Player", attr);
                }

                attr.clear();
                handler.startElement("", "", "id", attr);
                String id = model.getPlayerById(i).getID() + "";
                handler.characters(id.toCharArray(), 0, id.length());
                handler.endElement("", "", "id");

                attr.clear();
                handler.startElement("", "", "PlayerName", attr);
                String value = model.getPlayerById(i).getName() + "";
                handler.characters(value.toCharArray(), 0, value.length());
                handler.endElement("", "", "PlayerName");


                attr.clear();
                handler.startElement("", "", "playerTroops", attr);
                String troops = model.getPlayerById(i).getTroops() + "";
                handler.characters(troops.toCharArray(), 0, troops.length());
                handler.endElement("", "", "playerTroops");

                attr.clear();
                handler.startElement("", "", "ownTerritory", attr);
                for(Territory tempTerritory:model.getPlayerById(i).getTerritories()){
                    attr.clear();
                    handler.startElement("","","Territory",attr);

                    attr.clear();
                    handler.startElement("","","Name",attr);
                    handler.characters(tempTerritory.getName().toCharArray(),0,tempTerritory.getName().length());
                    handler.endElement("","","Name");

                    attr.clear();
                    handler.startElement("","","Troops",attr);
                    handler.characters(String.valueOf(tempTerritory.getTroops()).toCharArray(),0,String.valueOf(tempTerritory.getTroops()).length());
                    handler.endElement("","","Troops");

                    attr.clear();
                    handler.startElement("","","Holder",attr);
                    handler.characters(tempTerritory.getHolder().getName().toCharArray(),0,tempTerritory.getHolder().getName().length());
                    handler.endElement("","","Holder");

                    handler.endElement("","","Territory");
                }

                handler.endElement("", "", "ownTerritory");


                if(!model.getPlayerById(i).getContinents().isEmpty()){
                    attr.clear();
                    handler.startElement("","","ownContinent",attr);
                    for(Continent continent:model.getPlayerById(i).getContinents()){
                        attr.clear();
                        handler.startElement("","","Continent",attr);
                        handler.characters(continent.getName().toCharArray(),0,continent.getName().length());
                        handler.endElement("","","Continent");
                    }
                    handler.endElement("","","ownContinent");
                }

                if(model.getPlayerById(i) instanceof AIPlayer){
                    handler.endElement("", "", "AIPlayer");
                }else {
                    handler.endElement("", "", "Player");
                }
            }

            attr.clear();
            handler.startElement("", "", "Stage", attr);
            String stage = model.getCurrentStage().getName() + "";
            handler.characters(stage.toCharArray(), 0, stage.length());
            handler.endElement("", "", "Stage");

            attr.clear();
            handler.startElement("", "", "CurrentPlayer", attr);
            String currentplayer = model.getCurrentPlayer().getName() + "";
            handler.characters(currentplayer.toCharArray(), 0, currentplayer.length());
            handler.endElement("", "", "CurrentPlayer");

            attr.clear();
            handler.startElement("", "", "statusLabel", attr);
            statusLabel = model.getStatusString();
            handler.characters(statusLabel.toCharArray(), 0, statusLabel.length());
            handler.endElement("", "", "statusLabel");

            attr.clear();
            handler.startElement("", "", "originTerritoryButtonClick", attr);
            if(model.isOriginTerritoryButtonPressed())originTerritoryButtonClick = "true";
            else originTerritoryButtonClick = "false";
            handler.characters(originTerritoryButtonClick.toCharArray(), 0, originTerritoryButtonClick.length());
            handler.endElement("", "", "originTerritoryButtonClick");

            attr.clear();
            handler.startElement("", "", "targetTerritoryButtonClick", attr);
            if(model.isTargetTerritoryButtonPressed())targetTerritoryButtonClick = "true";
            else targetTerritoryButtonClick = "false";
            handler.characters(targetTerritoryButtonClick.toCharArray(), 0, targetTerritoryButtonClick.length());
            handler.endElement("", "", "targetTerritoryButtonClick");

            attr.clear();
            handler.startElement("", "", "originTerritoryName", attr);
            originTerritoryName = model.getOriginTerritoryName();
            handler.characters(originTerritoryName.toCharArray(), 0, originTerritoryName.length());
            handler.endElement("", "", "originTerritoryName");

            attr.clear();
            handler.startElement("", "", "targetTerritoryName", attr);
            targetTerritoryName = model.getTargetTerritoryName();
            handler.characters(targetTerritoryName.toCharArray(), 0, targetTerritoryName.length());
            handler.endElement("", "", "targetTerritoryName");

            handler.endElement("", "", "RISK");
            handler.endDocument();


        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
    }


    /**
     *import saved games with name
     */
    public void importXMLFileByName(String filename) throws Exception {
        String[] names = filename.split("_");

        model = new RiskModel(names[1]);
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        saxParser.parse(new File(filename + ".xml"), this);

    }

    @Override
    public void startElement(String u, String in, String qName, Attributes a) throws SAXException {
        if(qName.equalsIgnoreCase("Risk")) {
            try {
                model = new RiskModel(mapName);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(qName.equalsIgnoreCase("Player")) player = new Player("",this.model);;
        if(qName.equalsIgnoreCase("AIPlayer")) player = new AIPlayer("",this.model);;
        if(qName.equalsIgnoreCase("id")) isID =true;
        if(qName.equalsIgnoreCase("PlayerName")) isPlayerName = true;
        if(qName.equalsIgnoreCase("Territory")) territory = new Territory("");
        if(qName.equalsIgnoreCase("Continent")) isContinent = true;
        if(qName.equalsIgnoreCase("Name")) isTerritoryName = true;
        if(qName.equalsIgnoreCase("troops")) isTerritoryTroops = true;
        if(qName.equalsIgnoreCase("playerTroops")) isPlayerTroops = true;
        if(qName.equalsIgnoreCase("holder")) isHolder = true;
        if(qName.equalsIgnoreCase("Stage")) isStage = true;
        if(qName.equalsIgnoreCase("CurrentPlayer")) isCurrentPlayer = true;
        if(qName.equalsIgnoreCase("statusLabel")) isStatusLabel = true;
        if(qName.equalsIgnoreCase("originTerritoryButtonClick")) isOriginTerritoryButtonClick = true;
        if(qName.equalsIgnoreCase("targetTerritoryButtonClick")) isTargetTerritoryButtonClick = true;
        if(qName.equalsIgnoreCase("originTerritoryName")) isOriginTerritoryName = true;
        if(qName.equalsIgnoreCase("targetTerritoryName")) isTargetTerritoryName = true;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException{

        if(qName.equalsIgnoreCase("Player")){
            importPlayers.add(player);
        }
        if(qName.equalsIgnoreCase("AIPlayer")){
            importPlayers.add(player);
        }
        if(qName.equalsIgnoreCase("Territory")){
            player.addTerritory(territory);
        }
        if(qName.equalsIgnoreCase("Continent")){player.addContinent(tempContinent);}
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String info = new String(ch,start,length);
        if(isPlayerName){
            player.setName(info);
            isPlayerName = false;
        }
        if(isTerritoryName){
            territory.setName(info);
            isTerritoryName = false;
        }
        if(isHolder){
            territory.setHolder(player);
            isHolder = false;
        }
        if(isPlayerTroops){
            player.setTroops(Integer.parseInt(info));
            isPlayerTroops = false;
        }
        if(isTerritoryTroops){
            territory.setTroops(Integer.parseInt(info));
            isTerritoryTroops = false;
        }
        if(isStage){
            model.setCurrentStage(Stage.fromString(info));
            model.importPlayers(importPlayers);
            isStage = false;
        }
        if(isID){
            player.setID(Integer.parseInt(info));
            isID = false;
        }
        if(isCurrentPlayer){
            model.setCurrentPlayer(model.getPlayerByName(info));
            isCurrentPlayer = false;
        }
        if(isContinent){
            try {
                tempContinent = new Board(mapName).getContinentByName(info);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            isContinent = false;
        }
        if(isStatusLabel){
            model.setStatusString(info);
            isStatusLabel = false;
        }
        if(isOriginTerritoryButtonClick){
            if(info.equals("true"))model.setOriginTerritoryButtonPressed(true);
            else model.setOriginTerritoryButtonPressed(false);
            isOriginTerritoryButtonClick = false;
        }
        if(isTargetTerritoryButtonClick){
            if(info.equals("true"))model.setTargetTerritoryButtonPressed(true);
            else model.setTargetTerritoryButtonPressed(false);
            isTargetTerritoryButtonClick = false;
        }
        if(isOriginTerritoryName){
            model.setOriginTerritoryName(info);
            isOriginTerritoryName = false;
        }
        if(isTargetTerritoryName){
            model.setTargetTerritoryName(info);
            isTargetTerritoryName = false;
        }
    }

    /**Get the models
     *
     * @return the model
     */
    public RiskModel getModel() {
        return this.model;
    }

}