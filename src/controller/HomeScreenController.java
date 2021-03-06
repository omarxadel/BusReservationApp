package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import database.SQLiteDB;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import model.*;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import ui_components.AlertBox;

public class HomeScreenController implements Initializable {

	private Users user;
	private double xOffset, yOffset;
	private ToggleGroup menu, gender;
	private static final String WELCOME_MESSAGE = "Welcome Back ";
	private SQLiteDB db = SQLiteDB.getInstance();
	Node[] cards;
	Trip[] trips;

	@FXML
	private BorderPane root;
	@FXML
	private ToggleButton menu_home, menu_profile, menu_trips, menu_customer, menu_logout;
	@FXML
	private Label welcome_message, usernameProfile;
	@FXML
	private Circle minimize_btn, exit_btn, resize_btn;
	@FXML
	private GridPane card_view_holder;
	@FXML
	private TextField fieldSearch, fnameProfile, countryProfile, lnameProfile, cityProfile;
	@FXML
	private RadioButton femaleRadio, maleRadio;
	@FXML
	private Pane homeScreen, profileScreen;

	// --------------- INITIALIZE ---------------\\

    public HomeScreenController(){
        //TODO: Type constructor body
	}

	public void setUser(Users user){
    	this.user = user;
		// WELCOME MESSAGE
		welcome_message.setText(WELCOME_MESSAGE + user.getFirstname() + "!");
		initializeProfile();
	}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
		// MOVE THE WINDOW
		root.setOnMousePressed(mouseEvent -> {
			xOffset = root.getScene().getWindow().getX() - mouseEvent.getScreenX();
			yOffset = root.getScene().getWindow().getY() - mouseEvent.getScreenY();
		});

		root.setOnMouseDragged(mouseEvent -> {
			root.getScene().getWindow().setX(mouseEvent.getScreenX() + xOffset);
			root.getScene().getWindow().setY(mouseEvent.getScreenY() + yOffset);
		});

		// SIDE-MENU INITIALIZE
		menu = new ToggleGroup();
		menu_home.setToggleGroup(menu);
		menu_profile.setToggleGroup(menu);
		menu_trips.setToggleGroup(menu);
		menu_customer.setToggleGroup(menu);
		menu_logout.setToggleGroup(menu);

		menu_home.setSelected(true); // SELECT DEFAULT MENU

		// Get Trip Data
		populateCardViewHandler();

		// SEARCH LISTENER
		fieldSearch.textProperty().addListener((observable, oldValue, newValue) -> {
			card_view_holder.getChildren().removeAll(cards);
			for (int i = 0; i < trips.length; i++) {
				if ((trips[i].getStart().toLowerCase().contains(newValue.toLowerCase()) || trips[i].getDest().toLowerCase().contains(newValue.toLowerCase()) || trips[i].getType().toLowerCase().contains(newValue.toLowerCase()) || trips[i].getDate().toLowerCase().contains(newValue.toLowerCase()) || trips[i].getTime().toLowerCase().contains(newValue.toLowerCase()))) {
					try {
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(getClass().getResource("../ui_components/TripCard.fxml"));
						cards[i] = loader.load();

						TripCardController controller = loader.getController();

						controller.setLabels(trips[i].getStart(), trips[i].getDest(), trips[i].getDate(), trips[i].getTime(), Double.toString(trips[i].getPrice()));

						if(i%2 == 0)
							card_view_holder.addRow(i/2, cards[i]);
						else
							card_view_holder.addColumn(1, cards[i]);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		homeScreen.setVisible(true);
		profileScreen.setVisible(false);
    }

	private void populateCardViewHandler() {
    	cards = new Node[db.getTripsAmount()];
    	trips = db.loadTrips();
/*
		Node[] cards = new Node[20];
*/
		if(trips == null)
			AlertBox.display("ERROR", "NO TRIPS FOUND", "OK");
    	for (int i = 0 ; i < cards.length ; i ++){
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../ui_components/TripCard.fxml"));
				cards[i] = loader.load();

				TripCardController controller = loader.getController();

				controller.setLabels(trips[i].getStart(), trips[i].getDest(), trips[i].getDate(), trips[i].getTime(), Double.toString(trips[i].getPrice()));

				if(i%2 == 0)
					card_view_holder.addRow(i/2, cards[i]);
				else
					card_view_holder.addColumn(1, cards[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void initializeProfile() {
		// SET DATA
		usernameProfile.setText(user.getUsername());
		fnameProfile.setText(user.getFirstname());
		lnameProfile.setText(user.getLastname());
		cityProfile.setText(user.getCity());
		countryProfile.setText(user.getCountry());

		gender = new ToggleGroup();
		maleRadio.setToggleGroup(gender);
		femaleRadio.setToggleGroup(gender);

		if(user.getGender().toLowerCase().contains("male")) {
			maleRadio.setSelected(true);
			femaleRadio.setSelected(false);
		}
		else{
			maleRadio.setSelected(false);
			femaleRadio.setSelected(true);
		}
	}

	// --------------- HANDLE MOUSE CLICKS ---------------\\

	@FXML
	private void handleMouseClick(MouseEvent event){

    	if(event.getSource() == (menu_home)){
    		homeScreen.setVisible(true);
    		profileScreen.setVisible(false);
		}

    	if(event.getSource() == (menu_profile)){
    		homeScreen.setVisible(false);
    		profileScreen.setVisible(true);
		}

		if(event.getSource() == (menu_logout)){
			try {
				logout(event);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if(event.getSource() == (exit_btn)){
			System.exit(0);
		}
	}

	// --------------- MENU OPERATIONS ---------------\\

	private void logout(MouseEvent e) throws IOException {
		Parent Main = FXMLLoader.load(getClass().getResource("../view/MainMenu.fxml"));
		Scene MainScene = new Scene(Main);
		Stage stage = (Stage) (((Node) e.getSource()).getScene().getWindow());
		stage.setScene(MainScene);
	}

	private void getProfile (){
    	//TODO: LOAD PROFILE TO USER
	}
	
	/*public void profileButtonClicked (ActionEvent e)
	{
		ProfFull.setVisible(true);
		ProfTitle.setVisible(true);
		searchAnc.setVisible(false);
		SearchTabs.setVisible(false);
		PassengerTabs.setVisible(false);
		
		
	}
	
	
	public void customerService(ActionEvent e) {
		AlertBox.display("CUSTOMER SERVICE", "You can contact us directly through our email: busPlus@bplus.com", "OK");
	}

	public void returnProfButtonClicked (ActionEvent e)
	{   
		searchAnc.setVisible(true);
		SearchTabs.setVisible(true);
		PassengerTabs.setVisible(true);
		ProfFull.setVisible(false);
		ProfTitle.setVisible(false);

	}
	
	public void logOut(ActionEvent e) throws IOException {
		Parent Main = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
		Scene MainScene = new Scene(Main);
		
		Stage window = (Stage)(((Node) e.getSource()).getScene().getWindow());
		window.setScene(MainScene);
	}
	// --------------- Choosing Seats ---------------\\
	
	public void SeatingOptions (ActionEvent e) throws IOException
	{
		seatingTxt.setText("");
		SeatsDesign.display(reservedTrip, reservedTrip.seat.capacity);
		seatsChosen = SeatsDesign.getSeatsChosen();		
	}
	public Scene getScene() {
		return scene;
	}

	// --------------- Editing Profile Info ---------------\\
	
	public void initEditProfile() {
		FirstnameManager.setText(P.firstname);
		LastnameManager.setText(P.lastname);
		UsernameManager.setText(P.username);
		PasswordManager.setText(P.getPassword());
		RepassManager.setText(P.getPassword());
		CityManager.setText(P.city);
		CountryManager.setText(P.country);
		GenderManager.getItems().addAll("Male", "Female");
		GenderManager.setValue(P.gender);
	}
	
	public void toEditprofileClicked(ActionEvent e) {
		initEditProfile();
		editProfile.setVisible(true);
		ProfTitle.setVisible(false);
		ProfFull.setVisible(false);
	}
	
	public void returnFromEditProfile(ActionEvent e) {
		getProfile(d.P[currentuserIndex]);
		ProfTitle.setVisible(true);
		ProfFull.setVisible(true);
		editProfile.setVisible(false);
	}
	
	public static void displayDialogueBoxEdit(String title, String message, String buttonTxt, String buttonTxt2) {
		Stage window = new Stage();
		window.setTitle(title);
		window.setMinWidth(300);
		window.initModality(Modality.APPLICATION_MODAL);
		HBox internalLayout = new HBox(10);
		Button Button2 = new Button(buttonTxt);
		Button Button1 = new Button(buttonTxt2);
		internalLayout.getChildren().addAll(Button1, Button2);
		internalLayout.setAlignment(Pos.CENTER);
		Label error = new Label(message);
		Button1.setOnAction(e->{
			flag = false;
			window.close();
			});
		
		Button2.setOnAction(e-> {
			flag = true;
			window.close();
		});
		VBox layout = new VBox(10);
		layout.getChildren().addAll(error, internalLayout);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
	}
	
	public void saveProfileEditClicked(ActionEvent e) throws IOException {
		displayDialogueBoxEdit("MESSAGE","Are you sure you want to update the saved data?","Yes","No");
		if(flag) {
		if(FirstnameManager.getText() == null || LastnameManager.getText() == null || UsernameManager.getText() == null || PasswordManager.getText() == null || CityManager.getText() == null || CountryManager.getText() == null || GenderManager.getValue() == null) {
			if(FirstnameManager.getText().equals(null) || LastnameManager.getText().equals(null) || UsernameManager.getText().equals(null) || PasswordManager.getText().equals(null) || CityManager.getText().equals(null) || CountryManager.getText().equals(null) || GenderManager.getValue().equals(null)) {
				AlertBox.display("UNEXPECTED INPUTS!", "Make sure you fill in all the fields!", "OK");
			}
		}
		P.firstname = FirstnameManager.getText();
		P.lastname = LastnameManager.getText();
		P.username = UsernameManager.getText();
		P.setPassword(PasswordManager.getText());
		P.city = CityManager.getText();
		P.country = CountryManager.getText();
		P.gender = GenderManager.getValue();
		d.P[currentuserIndex] = P;
		d.saveManagerData();
		AlertBox.display("SUCCESS", "Account has been updated successfully!", "OK");
		returnFromEditProfile(e);
		
		}
		else return;
		
	}
	

//--------------- Search Trip Controls ---------------\\

	public void choiceInit() {
		type.getItems().addAll("One-Way", "Round-Trip");
		start.getItems().addAll(d.location.getLocations());
		destination.getItems().addAll(d.location.getLocations());
		type.setValue("One-Way");
		start.setValue("Brussels");
		destination.setValue("Amsterdam");
	}
	
	public String[] getSelection() {
		String [] searchData = new String [4];
		if(type.getValue() == null || start.getValue() == null || destination.getValue() == null || date.getValue() == null || start.getValue().equals(destination.getValue())) {
			AlertBox.display("UNEXPECTED INPUTS", "Make sure you enter all the search fields correctly!", "OK");
			return null;
		}
		else {
			searchData[0] = type.getValue();
			searchData[1] = start.getValue();
			searchData[2] = destination.getValue();
			searchData[3] = date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}
		return searchData;
	}
	
	public Trip[] queryTrips() {
		int i = 0, k =0;
		resultTrips = new Trip[100];
		String [] searchData;
		searchData = getSelection();
		while(d.T[i] != null) {
			if(d.T[i].type.equals(searchData[0]) && d.T[i].start.equals(searchData[1]) && d.T[i].destination.equals(searchData[2]) && d.T[i].date.equals(searchData[3])) {
				resultTrips[k] = d.T[i];
				k++;
			}
			i++;
		}
		return resultTrips;
	}
	
	public String [] showResults() {
		
		Trip [] T = queryTrips();
		String [] results = new String[50];
		int i = 0;
		while(T[i] != null) {
			results[i] = ("Trip type: " + T[i].type + "		Start: " + T[i].start + "		Destination: " + T[i].destination + "		Vehicle Type: " + T[i].seat.vtype + "		Free Seats: " + Integer.toString(T[i].seat.getFreeSeats())+ "		Date: " + T[i].date + "		Time: " + T[i].time +		" 	Price for one ticket: " + Float.toString(T[i].ticket));
			i++;
		}
		return results;
	}
	
	public void searchTableInit() {
		String [] results = showResults();
		int i = 0;
		while(results[i] != null) {
			searchView.getItems().add(results[i]);
			i++;
		}
	}

	
	public void searchButtonClicked(ActionEvent e) {
		searchTableInit();
		searchAnc.setVisible(true);
		searchView.setVisible(true);
		searchResultPane.setVisible(true);
		SearchTabs.setVisible(false);
		PassengerTabs.setVisible(false);
	}
	
	public void choiceReset() {
		type.setValue(null);
		start.setValue(null);
		destination.setValue(null);
		date.setValue(null);
		searchView.getItems().clear();
		
	}
	
	public void returnSearchTab(ActionEvent e) {
		choiceReset();
		SearchTabs.setVisible(true);
		PassengerTabs.setVisible(true);
		searchAnc.setVisible(true);
		searchResultPane.setVisible(false);
	}
	
	

//--------------- Booking a Trip ---------------\\

	public void getSearchSelection() {
		Trip thisTrip = resultTrips[searchView.getSelectionModel().getSelectedIndex()];
		startDestLabel.setText(thisTrip.start + " - " + thisTrip.destination);
		timeLabel.setText(thisTrip.time);
		dateLabel.setText(thisTrip.date);
		priceLabel.setText(Float.toString(thisTrip.ticket));
		seatsLabel.setText(Integer.toString(thisTrip.seat.getFreeSeats()));
		reservedTrip = thisTrip;
	}
	
	public void bookTripButtonClicked(ActionEvent e) {
		if(searchView.getSelectionModel().getSelectedIndex() == -1) {
			AlertBox.display("UNEXPECTED INPUTS", "Please make sure you selected a trip!", "OK");
		}
		else {
		getSearchSelection();
		booking.setVisible(true);
		searchAnc.setVisible(false);
		searchResultPane.setVisible(false);
		}
	}
	
	public void returnBookTripButtonClicked(ActionEvent e) {
		displayDialogueBox("MESSAGE ALERT","Are you sure you want to return? Any unsaved data will be lost!" , "Proceed" , "Abort");
		if(flag) {
			searchAnc.setVisible(true);
			searchResultPane.setVisible(true);
			booking.setVisible(false);
		}
		else return;
		
	}
	
	public static void displayDialogueBox(String title, String message, String buttonTxt, String buttonTxt2) {
		Stage window = new Stage();
		window.setTitle(title);
		window.setMinWidth(300);
		window.initModality(Modality.APPLICATION_MODAL);
		HBox internalLayout = new HBox(10);
		Button Button2 = new Button(buttonTxt);
		Button Button1 = new Button(buttonTxt2);
		internalLayout.getChildren().addAll(Button1, Button2);
		internalLayout.setAlignment(Pos.CENTER);
		Label error = new Label(message);
		Button1.setOnAction(e->{
			flag = false;
			window.close();
			});
		
		Button2.setOnAction(e-> {
			flag = true;
			window.close();
		});
		VBox layout = new VBox(10);
		layout.getChildren().addAll(error, internalLayout);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
	}
	
	
	public void bookTripClicked(ActionEvent e) throws IOException {
		paymentDisplay();
		showUnpaidTickets();
		
	}

	//--------------- Payment Window ---------------\\
	
	
	public int ticketSerialGenerator() {
		int serial = 225000 , i = 0 ;
		while(d.Tk[i] != null) {
			if(d.Tk[i].serial == serial) {
				serial++;
				i++;
				continue;
			}
			else {
				i++;
				continue;
			}
		}
		i=0;
		if(upaid == null) {
			
		}
		else {
	while(upaid[i] != null) {
			if(upaid[i].serial == serial) {
				serial++;
				i++;
				continue;
			}
			else {
				i++;
				continue;
			}
		}}
		return serial;
	}

	public void paymentDisplay() {
		payment.setVisible(true);
		booking.setVisible(false);
	}
	
	public void getPaymentMethod(String paymentMethod) throws IOException {
		if(paymentMethod.equals("Cash")) {
			pay = new Cash();
			book(1);
		}
		else if(paymentMethod.equals("Credit")) {
			pay = new CreditCard();
			boolean flag = PaymentWindow.display("Credit Card", "Payment Information", "Card", "Return", "Pay "+totalCheck + " �", "Card Number");
			if(flag) {
				String credit = PaymentWindow.code;
				if(d.credit.validateCreditCard(credit)) {
					if(d.credit.sufficient(totalCheck, credit)) {
						pay.pay(totalCheck, credit);
						book(2);
					}
					else AlertBox.display("ERROR", "Not sufficient balance!", "OK");
				}
				else AlertBox.display("ERROR", "Not an existing credit card number!", "OK");
				return;
			}
		}
		else if(paymentMethod.equals("Promo")) {
			pay = new PromoCode();
			boolean flag = PaymentWindow.display("Promo Code", "Payment Information", "Promo", "Return", "Enter", "Promo Code");
			if(flag) {
				String credit = PaymentWindow.code;
				int i = 0;
				if(d.promo.validatePromoCode(credit)) {
					while(promocode[i] != null) {
						if(promocode[i].equals(credit)) {
							AlertBox.display("ERROR", "You can't use a promo code twice at the same session", "OK");
							i++;
							return;
							
						}}
				
					d.promo.pay(totalCheck, credit);
					totalCheck = d.promo.getNewPrice();
					promocode[pIndex] = credit;
					book(3);
				}
				else AlertBox.display("ERROR", "Not an existing promo code!", "OK");
			}
			return;
		}
	}
	
	public Ticket[] getUnpaidTickets() {
		int index=0;
		Ticket [] unpaidTickets = new Ticket[50];
		if(seatingTxt.getText().equals(null) || seatingTxt.getText() == null) {
			if(seatsChosen == null) {
				AlertBox.display("UNEXPECTED INPUTS", "Please make sure you chose at least 1 seat!", "OK");
				return null;
			}
			else {
				int i = 0;
				Ticket tmp = null;
				while(seatsChosen[i] != null)
					tmp = reserve.makeReservation(reservedTrip, P.username,ticketSerialGenerator(), seatsChosen[i], "UNPAID");
					totalCheck += tmp.price;
					unpaidTickets[index] = tmp;
					i++;
					index++;
			}
		}
		else {
			if(seatsChosen!=null) {
					int i = 0;
					Ticket tmp = null;
					while(seatsChosen[i] != null) {
						tmp = reserve.makeReservation(reservedTrip, P.username, ticketSerialGenerator(), seatsChosen[i], "UNPAID");
						totalCheck += tmp.price;
						unpaidTickets[index] = tmp;
						i++;
					}
					upaid=unpaidTickets;
			}			
			else if(seatsChosen == null) {
				int numSeats;
			
				try{
					numSeats = Integer.parseInt(seatingTxt.getText());
					if(numSeats > reservedTrip.seat.getFreeSeats()) {
						AlertBox.display("UNEXPECTED INPUTS", "Enter a valid number of seats!", "OK");
						return null;
					}
				}
				catch(NumberFormatException exception) {
					AlertBox.display("UNEXPECTED INPUTS", "Enter a valid number of seats!", "OK");
					seatingTxt.setText("");
					return null;
				}
				while(numSeats != 0) {
					Ticket tmp = reserve.makeReservation(reservedTrip, P.username, ticketSerialGenerator(), reservedTrip.seat.bookRandom(), "UNPAID");
					totalCheck += tmp.price;
					unpaidTickets[index] = tmp;
					upaid = unpaidTickets;
					index++;
					numSeats --;
					
				}
			}
		}
		
		return unpaidTickets;
	}
	
	
	public void showUnpaidTickets() {
		int i = 0;
		Ticket[] unpaid = getUnpaidTickets();
		String unpaidData;
		while(unpaid[i] != null) {
			unpaidData = (unpaid[i].serial + " " + unpaid[i].T.destination + " " + unpaid[i].T.start + " " + unpaid[i].T.date + " " + unpaid[i].T.time + " " + unpaid[i].price + " " + unpaid[i].seat);
			purchaseView.getItems().add(unpaidData);
			i++;
		}
		checkTotal.setText(Float.toString(totalCheck) + " �");
	}
	
	public void bookNowClicked(ActionEvent e) {
		showUnpaidTickets();
	}
	
	public void cashButtonClicked(ActionEvent e) throws IOException {
		getPaymentMethod("Cash");
	}
	
	public void creditButtonClicked(ActionEvent e) throws IOException {
		getPaymentMethod("Credit");
	}
	
	public void promoButtonClicked(ActionEvent e) throws IOException {
		getPaymentMethod("Promo");
	}
	
	public void clearUnpaidTicket() {
		purchaseView.getItems().clear();
		checkTotal.setText(null);
		searchResultPane.setVisible(true);
		booking.setVisible(false);
		payment.setVisible(false);
		totalCheck = 0;
		reservedTrip = null;
		upaid = null;
		
	}
	
	
	public void book(int type) throws IOException {
		Ticket [] unpaidTickets = upaid;
		int i = 0;
		while(unpaidTickets[i] != null) {
			if(type == 1) {
				d.addTicket(unpaidTickets[i].T, P.username, unpaidTickets[i].serial, unpaidTickets[i].seat , unpaidTickets[i].price, "Cash");
			}
			else if(type == 3) {
				checkTotal.setText(Float.toString(totalCheck) + " �");
				AlertBox.display("SUCCESS", "Your total check has been reduced by "+d.promo.promocodes.get(promocode[pIndex]) + " %", "Continue");
				pIndex++;
				return;
			}
			else {
				d.addTicket(unpaidTickets[i].T, P.username, unpaidTickets[i].serial, unpaidTickets[i].seat , unpaidTickets[i].price, "CreditCard");
			}
			i++;
		}
		d.saveTripData();
		clearUnpaidTicket();
		AlertBox.display("SUCCESS", "Your reservation has been made successfully! You may now view it from MyTrips tab!", "OK");
		
	}
	
	public void returnPaymentClicked(ActionEvent e) {
		purchaseView.getItems().clear();
		int i = 0;
		while(upaid[i] != null) {
			reservedTrip.seat.resetByName(upaid[i].seat);
			i++;
		}
		
		booking.setVisible(true);
		payment.setVisible(false);
	}
	
	
	
	
//--------------- Trips Schedule Tab ---------------\\

	public void SelectTrip (ActionEvent e) throws IOException
	{
		LabelChoose.setVisible(true);
		back.setVisible(true);
		if(line1.getTypeSelector() != null)
		{
			Trip1.setVisible(true);
		}
		SearchTabs.setVisible(false);
		PassengerTabs.setVisible(false);
		
	}
	public void BackMainTab (ActionEvent e) throws IOException
	{
		
		SearchTabs.setVisible(true);
		PassengerTabs.setVisible(true);
		Trip1.setVisible(false);
		LabelChoose.setVisible(false);
		back.setVisible(false);
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		choiceInit();
	}
	
	//--------------- MyTrips View ---------------\\
	
	
	public Ticket[] queryMyTrips() {
		int i = 0, k =0;
		resultTickets = new Ticket[100];
		while(d.Tk[i] != null) {
			if(d.Tk[i].uname.equals(P.username)){
				resultTickets[k] = d.Tk[i];
				k++;
			}
			i++;
		}
		return resultTickets;
	}
	
	public String [] showMyResults() {
		Ticket [] Tk = queryMyTrips();
		Ticketresults = new String[50];
		int i = 0;
		while(Tk[i] != null) {
			Ticketresults[i] = ("Ticket Number: " + Tk[i].serial + "		Destination: "+ Tk[i].T.destination + "		Start: " + Tk[i].T.start + "		Ticket Price: " + Tk[i].price + "		Payment Method Chosen: " + Tk[i].payment);
			i++;
		}
		return Ticketresults;
	}
	
	public void myTripTableInit() {
		String [] results = showMyResults();
		int i = 0;
		while(results[i] != null) {
			myTrips.getItems().add(results[i]);
			i++;
		}
	}

	
	public void myTripsClicked(ActionEvent e) {
		myTripTableInit();
		myTripsPane.setVisible(true);
		myTrips.setVisible(true);
		searchResultPane.setVisible(false);
		SearchTabs.setVisible(false);
		PassengerTabs.setVisible(false);
	}
	
	public void myTripsReset() {
		myTrips.getItems().clear();
		
	}
	
	public void returnMyTrips(ActionEvent e) {
		myTripsReset();
		SearchTabs.setVisible(true);
		PassengerTabs.setVisible(true);
		myTripsPane.setVisible(false);
	}
	
	
	public void showTripDetails(ActionEvent e) throws IOException {
			System.out.println("CLICK");
			Ticket[] T = queryMyTrips();
			int index = myTrips.getSelectionModel().getSelectedIndex();
			Ticket selection = T[index];
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("ShowTicket.fxml"));
			Parent Show =(Parent) loader.load();
			ShowTicketController controller = loader.getController();
			controller.addData(selection.uname, selection.T.start, selection.T.destination, selection.T.time, selection.T.date, Integer.toString(selection.T.ID));
			Stage window = new Stage();
			window.setTitle("Ticket Details");
			window.setScene(new Scene(Show));
			window.showAndWait();
		
	}
	
	
	*/
	
}
