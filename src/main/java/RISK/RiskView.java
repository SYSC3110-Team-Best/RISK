package RISK;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * The View in the mvc
 */
public class RiskView extends JFrame implements RiskViewInterface{
    private JLabel statusLabel = new JLabel();
    private JLabel continentsLabel = new JLabel();
    private JLabel troopsLabel = new JLabel("Troops:");

    private final int buttonWidthHeight = 30;

    private JPanel mainPanel = new JPanel();
    private JPanel operationPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();
    private JPanel decideButtonPanel = new JPanel();

    private MapPanel graphMapPanel;

    private JComboBox troopsBox = new JComboBox();

    private JScrollPane continentInfoPane;

    private JMenuItem saveItem = new JMenuItem("Save");
    private JMenuItem loadItem = new JMenuItem("Load");
    private JMenuItem newGameItem = new JMenuItem("New");
    private JMenuItem newMapItem = new JMenuItem("Load New Map");
    private JMenu fileMenu = new JMenu("File");
    private JMenuBar menuBar = new JMenuBar();

    private JButton attack = new JButton("Attack");
    private JButton draft = new JButton("Draft");
    private JButton fortify = new JButton("Fortify");
    private JButton deploy = new JButton("Deploy");
    private JButton skipButton = new JButton("Skip");
    private JButton confirmButton = new JButton("Confirm");

    private ArrayList<JButton> commandButtonList = new ArrayList<>();
    private ArrayList<JButton> territoryButtons = new ArrayList<>();

    private int numberPlayer = 0;

    private PlayerSettingDialog playerSettingDialog;

    /**
     * Initial frame
     */
    public RiskView(Board board){
        super("Risk Game");

        graphMapPanel = new MapPanel(board);
        graphMapPanel.setPreferredSize(new Dimension(850,589));
        graphMapPanel.setLayout(null);

        HashMap<String,ArrayList<Integer>> coordinates = board.getCoordinates();
        for(String territoryName: coordinates.keySet()){
            ArrayList<Integer> xy = coordinates.get(territoryName);
            JButton button = new JButton();
            graphMapPanel.add(button);
            territoryButtons.add(button);
            button.setBounds(xy.get(0),xy.get(1),buttonWidthHeight,buttonWidthHeight);
            button.setActionCommand(territoryName);
        }

        continentInfoPane = new JScrollPane(continentsLabel);
        continentInfoPane.setPreferredSize(new Dimension(200,600));
        continentsLabel.setFont(new Font("Arial",0,12));

        buttonPanel.setLayout(new GridLayout(2,2));
        buttonPanel.add(draft);
        buttonPanel.add(attack);
        buttonPanel.add(deploy);
        buttonPanel.add(fortify);

        decideButtonPanel.setLayout(new GridLayout(1,2));
        decideButtonPanel.add(confirmButton);
        decideButtonPanel.add(skipButton);

        troopsLabel.setAlignmentX(BOTTOM_ALIGNMENT);
        statusLabel.setFont(new Font("Arial",1,20));

        operationPanel.setLayout(new BoxLayout(operationPanel,BoxLayout.Y_AXIS));
        operationPanel.add(buttonPanel);
        operationPanel.add(troopsLabel);
        operationPanel.add(troopsBox);

        operationPanel.add(decideButtonPanel);
        operationPanel.setPreferredSize(new Dimension(200,600));


        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.add(newGameItem);
        fileMenu.add(newMapItem);
        menuBar.add(fileMenu);

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(statusLabel,BorderLayout.NORTH);
        mainPanel.add(graphMapPanel,BorderLayout.WEST);
        mainPanel.add(operationPanel,BorderLayout.CENTER);
        mainPanel.add(continentInfoPane,BorderLayout.EAST);

         attack.setEnabled(false);
         draft.setEnabled(false);
         fortify.setEnabled(false);
         deploy.setEnabled(false);
         skipButton.setEnabled(false);
         confirmButton.setEnabled(false);

        statusLabel.setText("To start a new game: File -> New.");
        this.setJMenuBar(menuBar);
        menuBar.setSize(50, 28);
        this.add(mainPanel);

        this.commandButtonList.add(draft);
        this.commandButtonList.add(attack);
        this.commandButtonList.add(fortify);
        this.commandButtonList.add(deploy);
        this.commandButtonList.add(skipButton);
        this.commandButtonList.add(confirmButton);

        this.setLocation(50,50);
        this.setSize(1000,4000);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);

        for(JButton button:territoryButtons){
            button.setMargin(new Insets(0,0,0,0));
            button.setFont(new Font("Arial",Font.BOLD,12));
            button.setOpaque(true);
            //Windows default border for JButton. Set it to make color in enabled button visible
            button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        }
    }

    /**
     * This method is prompting to get the number of players.
     *
     * @return the number of player
     */
    @Override
    public int getNumberPlayerDialog(){
        int numberPlayer = 0;
        while(numberPlayer==0) {
            String input = JOptionPane.showInputDialog( "Please enter the number of players.(2-6)");
            try {
                if (!input.matches("[2-6]"))
                    JOptionPane.showMessageDialog(null, "Invalid Input.", "Warning", JOptionPane.ERROR_MESSAGE);
                else {
                    numberPlayer = Integer.parseInt(input);
                }
            }catch (NullPointerException e){
                JOptionPane.showMessageDialog(null, "Invalid Input.", "Warning", JOptionPane.ERROR_MESSAGE);
            }
        }
        this.numberPlayer = numberPlayer;
        return numberPlayer;
    }


    /**
     * This method is prompting to get each player's name
     *
     * @return the player's name string list
     */
    @Override
    public LinkedHashMap<String,Boolean> popGetName(){
        playerSettingDialog = new PlayerSettingDialog(this.numberPlayer);

        return playerSettingDialog.getFinalNamesAndIsAI();
    }

    /**
     * This method is a Setter for continentsLabel
     *
     * @param string
     */
    public void setContinentsLabel(String string){
        continentsLabel.setText(string);
    }

    @Override
    public void updateWinAttack(Player currentPlayer) {
        deploy.setEnabled(true);
        confirmButton.setEnabled(false);
        skipButton.setEnabled(false);
        disableAllTerritoryButton();
        setStatusLabel(currentPlayer.getName()+" wins the battle! Press \"Deploy\" button to deploy troops.");
        clearTroopsBox();
    }

    /**
     *This method is getting the operating JButton
     *
     * @param buttonText the button text
     * @return the JButton that contain the buttonText
     */
    public JButton getJButton(String buttonText) {

        for (JButton button : commandButtonList){
            if (button.getText() == buttonText)
                return button;
        }
        return null;
    }

    /**
     * This method is setting status Label
     *
     * @param str set the text in status label
     */
    @Override
    public void setStatusLabel(String str){
        statusLabel.setText(str);
    }

    @Override
    public String getStatusLabel(){
        return statusLabel.getText();
    }

    @Override
    public void paintOriginAndTargetTerritory(boolean originTerritoryButtonPressed, boolean targetTerritoryButtonPressed, String originTerritoryName, String targetTerritoryName) {
        if(!originTerritoryButtonPressed){
            getTerritoryButtonByString(originTerritoryName).setBackground(Color.RED);
        }
        if(!targetTerritoryButtonPressed){
            getTerritoryButtonByString(targetTerritoryName).setBackground(Color.ORANGE);
        }
    }

    @Override
    public void updateAIAttack(Player currentPlayer, Territory tempAttackTerritory, Territory tempDefenceTerritory) {
        setStatusLabel(currentPlayer.getName()+"'s turn, Attack Stage. "+tempAttackTerritory.getName()+" attacks "+tempDefenceTerritory.getName()+".");
        getTerritoryButtonByString(tempAttackTerritory.getName()).setBackground(Color.RED);
        getTerritoryButtonByString(tempDefenceTerritory.getName()).setBackground(Color.ORANGE);
        disableAllCommandButtons();
    }

    @Override
    public void disableAllCommandButtons() {
        for(JButton button:commandButtonList){
            button.setEnabled(false);
        }
    }

    @Override
    public void updateAIFortify(Player currentPlayer,String fortifyTerritoryName, String fortifiedTerritoryName) {
        getTerritoryButtonByString(fortifyTerritoryName).setBackground(Color.RED);
        getTerritoryButtonByString(fortifiedTerritoryName).setBackground(Color.ORANGE);
        setStatusLabel(currentPlayer.getName()+"'s turn, Fortify Stage. Move troops from "+fortifyTerritoryName+" to "+fortifiedTerritoryName+".");
        disableAllTerritoryButton();
    }

    @Override
    public void updateDraftCountryClick(String territoryName) {
        onlyEnableOriginTerritory(territoryName);
        getTerritoryButtonByString(territoryName).setBackground(Color.RED);
    }

    @Override
    public void updateAIDeploy(String statusLabel) {
        disableAllCommandButtons();
        setStatusLabel(statusLabel);
        fortify.setEnabled(true);
    }

    @Override
    public void updateAIWinAttack() {
        deploy.setEnabled(true);
    }


    /**
     * This method is setting the JComboBox with troops available in different stage.
     *
     * @param troops set the numbers in troops box
     */
    public void setTroopsBox(int troops){
        troopsLabel.setText("Troops:");
        troopsBox.removeAllItems();
        for(int i=1; i<=troops; i++){
            troopsBox.addItem(i);
        }
    }

    /**
     * This method is setting the JComboBox with ways to attack.
     *
     * @param troops set the way to attack:one,two,three, and blitz
     */
    @Override
    public void setAttackTroopsBox(int troops){
        troopsLabel.setText("Way to attack:");
        troopsBox.removeAllItems();
        troopsBox.addItem(AttackWay.BLITZ);
        troopsBox.addItem(AttackWay.ONE);
        if(troops>2)troopsBox.addItem(AttackWay.TWO);
        if(troops>3)troopsBox.addItem(AttackWay.THREE);
    }

    /**
     * This method is clearing the troop box
     */
    public void clearTroopsBox(){
        troopsBox.removeAllItems();
    }

    /**
     * This method is getting the attackway for attack stage in JComboBox
     *
     * @return the troop number in troops box that selected
     */
    @Override
    public AttackWay getAttackTroopsBox(){
        return (AttackWay) troopsBox.getSelectedItem();
    }


    /**
     * This method is setting the troops onto the territory button.
     * @param territoryName
     * @param troops
     */
    public void setTerritoryButtonTroops(String territoryName, int troops){
        for(JButton button:territoryButtons){
            if(button.getActionCommand().equals(territoryName))button.setText(String.valueOf(troops));
        }
    }

    @Override
    public void updateNewGameProcess(String mapInfoThroughContinent, String currentPlayerName) {
        setContinentsLabel(mapInfoThroughContinent);
        draft.setEnabled(true);
        setStatusLabel("Now it's "+ currentPlayerName +"'s turn, please click \"Draft\" button to start DRAFT stage.");
        disableAllTerritoryButton();
    }

    /**
     * This method is for Disable all territory buttons
     */
    @Override
    public void disableAllTerritoryButton(){
        for(JButton button:territoryButtons){
            button.setEnabled(false);
        }
    }

    @Override
    public void enableButton(String buttonName) {
        getJButton(buttonName).setEnabled(true);
    }

    /**
     *This methods is making the original territories button enabled
     *
     * @param territories
     */
    @Override
    public void enableOriginalTerritories(ArrayList<Territory> territories){
        for(Territory territory:territories){
            for(JButton button:territoryButtons){
                if(button.getActionCommand().equals(territory.getName())){
                    button.setEnabled(true);
                    break;
                }
            }
        }
    }

    @Override
    public void updateDeployFinish(Player currentPlayer) {
        getJButton("Attack").setEnabled(true);
        getJButton("Confirm").setEnabled(false);
        getJButton("Skip").setEnabled(true);
        setStatusLabel(currentPlayer.getName() +"'s turn, please click \"Attack\" button to continue ATTACK stage OR \"Skip\" button to skip to Fortify Stage.");
        disableAllTerritoryButton();
    }

    @Override
    public void updateFortifyFinish(Player currentPlayer) {
        disableAllCommandButtons();
        draft.setEnabled(true);
        setStatusLabel(currentPlayer.getName()+ "'s turn, please click \"Draft\" button to start DRAFT stage.");
        disableAllTerritoryButton();
    }

    /**
     * Only enable one territory button and disable all others
     *
     * @param territoryName
     */
    public void onlyEnableOriginTerritory(String territoryName){
        disableAllTerritoryButton();
        for(JButton button:territoryButtons){
            if(button.getActionCommand().equals(territoryName)){
                button.setEnabled(true);
                return;
            }
        }
    }

    @Override
    public void updateClickAttackTerritoryButton(int attackTerritoryTroops, ArrayList<Territory> defenceTerritories, String attackTerritoryName) {
        getTerritoryButtonByString(attackTerritoryName).setBackground(Color.RED);
        setAttackTroopsBox(attackTerritoryTroops);
        enableTargetTerritories(defenceTerritories, attackTerritoryName);
    }

    @Override
    public void updateClickTargetTerritoryButton(String originTerritoryName, String targetTerritoryName) {
        getTerritoryButtonByString(targetTerritoryName).setBackground(Color.ORANGE);
        disableAllTerritoryButton();
        enableTerritoryButton(originTerritoryName);
        enableTerritoryButton(targetTerritoryName);
    }

    @Override
    public void updateCancelDefenceTerritoryButton(String originTerritoryName, ArrayList<Territory> defenceTerritories) {
        getTerritoryButtonByString(originTerritoryName).setBackground(Color.RED);
        onlyEnableOriginTerritory(originTerritoryName);
        enableTargetTerritories(defenceTerritories, originTerritoryName);
    }

    @Override
    public void updateClickFortifyButton(int fortifyTroops, ArrayList<Territory> fortifiedTerritory, String originTerritoryName) {
        getTerritoryButtonByString(originTerritoryName).setBackground(Color.RED);
        setTroopsBox(fortifyTroops);
        enableTargetTerritories(fortifiedTerritory,originTerritoryName);
    }

    @Override
    public void updateCancelFortifyTerritoryButton(String originTerritoryName, ArrayList<Territory> fortifiedTerritory) {
        getTerritoryButtonByString(originTerritoryName).setBackground(Color.RED);
        onlyEnableOriginTerritory(originTerritoryName);
        enableTargetTerritories(fortifiedTerritory,originTerritoryName);
    }

    @Override
    public void updateAIDraft(Player currentPlayer, String continentBonusString, Territory draftTerritory) {
        setStatusLabel(currentPlayer.getName() + "'s turn, Draft stage. You have " + currentPlayer.getTroops() + " troops can be sent."+continentBonusString);
        getTerritoryButtonByString(draftTerritory.getName()).setBackground(Color.RED);
        disableAllCommandButtons();
        getJButton("Attack").setEnabled(true);
    }

    /**
     * This method is for enable one territory button without disable others
     *
     * @param territoryName the territory name
     */
    public void enableTerritoryButton(String territoryName){
        for(JButton button:territoryButtons){
            if(button.getActionCommand().equals(territoryName)){
                button.setEnabled(true);
                return;
            }
        }
    }

    /**
     * Enable the target territories buttons
     *
     * @param territories     the territories
     * @param originTerritory the origin territory
     */
    public void enableTargetTerritories(ArrayList<Territory>territories, String originTerritory){
        onlyEnableOriginTerritory(originTerritory);
        for(Territory territory:territories){
            for(JButton button:territoryButtons){
                if(button.getActionCommand().equals(territory.getName())){
                    button.setEnabled(true);
                    break;
                }
            }
        }
    }

    /**
     * Get territory button by string j button.
     *
     * @param territoryName the territory name
     * @return the j button
     */
    public JButton getTerritoryButtonByString(String territoryName){
        for(JButton button:territoryButtons){
            if(button.getActionCommand().equals(territoryName))return button;
        }
        return null;
    }

    /**
     * paint the territory's button for the player
     *
     * @param player
     * @param color
     */
    public void paintTerritoryButtons(Player player,Color color){
        for(Territory territory:player.getTerritories()){
            for(JButton button:territoryButtons){
                if(territory.getName().equals(button.getActionCommand())){
                    button.setBackground(color);
                    continue;
                }
            }
        }
    }

    @Override
    public void updateDraftPrepare(Player currentPlayer, Stage currentStage, String continentBonus) {
        confirmButton.setEnabled(true);
        draft.setEnabled(false);
        setStatusLabel(currentPlayer.getName() + "'s turn, " + currentStage.getName() + " stage. You have " + currentPlayer.getTroops() + " troops can be sent."+continentBonus);
        setTroopsBox(currentPlayer.getTroops());
        enableOriginalTerritories(currentPlayer.getTerritories());
    }

    @Override
    public void updateAttackPrepare(Player currentPlayer, Stage currentStage, ArrayList<Territory> attackTerritoriesList) {
        attack.setEnabled(false);
        confirmButton.setEnabled(true);
        skipButton.setEnabled(true);
        enableOriginalTerritories(attackTerritoriesList);
        setStatusLabel(currentPlayer.getName() + "'s turn, " + currentStage.getName() + " stage. Click enabled territory button and pick surround available target territory button.");
    }

    @Override
    public void updateFortifyPrepare(ArrayList<Territory> fortifyTerritories, Player currentPlayer, Stage currentStage) {
        disableAllTerritoryButton();
        skipButton.setEnabled(true);
        fortify.setEnabled(false);
        confirmButton.setEnabled(true);
        enableOriginalTerritories(fortifyTerritories);
        setStatusLabel(currentPlayer.getName() + "'s turn, " + currentStage.getName() + " stage. Please choose the two Territories you want send troops from and to.");
    }

    @Override
    public void updateDeployPrepare(Player currentPlayer, Stage currentStage, int attackTroops) {
        deploy.setEnabled(false);
        attack.setEnabled(false);
        confirmButton.setEnabled(true);
        skipButton.setEnabled(false);
        setStatusLabel(currentPlayer.getName() + "'s turn, " + currentStage.getName() + " stage. Set troops to the new earned territory.");
        setTroopsBox(attackTroops-1);
    }

    @Override
    public void updateSkipAttack(Player currentPlayer) {
        attack.setEnabled(false);
        fortify.setEnabled(true);
        confirmButton.setEnabled(false);
        skipButton.setEnabled(false);
        disableAllTerritoryButton();
        setStatusLabel(currentPlayer.getName() + "'s turn, Click \"Fortify\" button to start Fortify stage.");
    }

    @Override
    public void updateSkipFortify(Player currentPlayer) {
        fortify.setEnabled(false);
        skipButton.setEnabled(false);
        draft.setEnabled(true);
        confirmButton.setEnabled(false);
        setStatusLabel(currentPlayer.getName()+ "'s turn, please click \"Draft\" button to start DRAFT stage.");
        disableAllTerritoryButton();
    }

    @Override
    public void resetButtonsAndBox(ArrayList<Territory> attackTerritoriesList) {
        clearTroopsBox();
        disableAllTerritoryButton();
        enableOriginalTerritories(attackTerritoriesList);
    }

    /**
     * Get the selected troops in JComboBox and cast into int
     *
     * @return the troops number that be selected
     */
    @Override
    public int getSelectedTroops(){
        return (int) troopsBox.getSelectedItem();
    }

    @Override
    public void updateDraftProcess(Player currentPlayer, Stage currentStage) {
        setStatusLabel(currentPlayer.getName() + "'s turn, " + currentStage.getName() + " stage. You have " + currentPlayer.getTroops() + " troops can be sent.");
        setTroopsBox(currentPlayer.getTroops());
    }

    @Override
    public void updateDraftFinish(Player currentPlayer) {
        disableAllTerritoryButton();
        confirmButton.setEnabled(false);
        attack.setEnabled(true);
        skipButton.setEnabled(false);
        setStatusLabel(currentPlayer.getName() +"'s turn, please click \"Attack\" button to start ATTACK stage.");

    }

    /**
     * Add new action listener for newGame button
     *
     * @return file in string
     */
    public String getFileName(){
        String file = JOptionPane.showInputDialog( "Please enter the file name");
        return file;
    }

    /**
     * Add new action listener for newGame button
     *
     * @param newGameListener the new game listener
     */
    public void addNewGameMenuListener(ActionListener newGameListener){
        newGameItem.addActionListener(newGameListener);
    }

    /**
     * Add new action listener for draft button
     *
     * @param draftButtonListener the draft button listener
     */
    public void addDraftButtonListener(ActionListener draftButtonListener){
        draft.addActionListener(draftButtonListener);
    }

    /**
     * Add new action listener for attack button
     *
     * @param attackButtonListener the attack button listener
     */
    public void addAttackButtonListener(ActionListener attackButtonListener){
        attack.addActionListener(attackButtonListener);
    }


    /**
     * Add new action listener for fortify button
     *
     * @param fortifyButtonListener
     */
    public void addFortifyButtonListener(ActionListener fortifyButtonListener){
        fortify.addActionListener(fortifyButtonListener);
    }

    /**
     * Add new action listener for deploy button
     *
     * @param deployButtonListener
     */
    public void addDeployButtonListener(ActionListener deployButtonListener){
        deploy.addActionListener(deployButtonListener);
    }

    /**
     * Add new action listener for skip button
     *
     * @param skipButtonListener
     */
    public void addSkipButtonListener(ActionListener skipButtonListener){
        skipButton.addActionListener(skipButtonListener);
    }

    /**
     * Add new action listener for confirm button
     *
     * @param confirmButtonListener
     */
    public void addConfirmButtonListener(ActionListener confirmButtonListener){
        confirmButton.addActionListener(confirmButtonListener);
    }


    /**
     * Add new action listener for territory Button
     *
     * @param territoryButtonListener
     */
    public void addTerritoryButtonListener(ActionListener territoryButtonListener){
        for(JButton button:territoryButtons){
            button.addActionListener(territoryButtonListener);
        }
    }

    /**
     * Add new action listener for saving Button
     *
     * @param saveButtonListener
     */
    public void addSaveButtonListener(ActionListener saveButtonListener){
        saveItem.addActionListener(saveButtonListener);
    }

    /**
     * Add new action listener for importing Button
     *
     * @param importButtonListener
     */
    public void addImportButtonListener(ActionListener importButtonListener){
        loadItem.addActionListener(importButtonListener);
    }

    /**
     * Add new action listener for new map button
     *
     * @param newMapListener
     */
    public void addNewMapButtonListener(ActionListener newMapListener){
        newMapItem.addActionListener(newMapListener);
    }

}


