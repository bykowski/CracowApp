package com.cracowapp.visitcracow.gui;

import com.cracowapp.visitcracow.client.ImageClient;
import com.cracowapp.visitcracow.service.GoogleMyResultsMapCreator;
import com.cracowapp.visitcracow.client.GooglePlacesClient;
import com.cracowapp.visitcracow.model.Result;
import com.google.common.base.Joiner;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Route
public class KrakowGui extends VerticalLayout{

    @Autowired
    public KrakowGui(GooglePlacesClient googlePlacesClient, GoogleMyResultsMapCreator googleMyResultsMapCreator,
                     ImageClient imageClient) {

        Label labelWelcome = new Label("WELCOME TO KRAKOW - DISCOVER THE CITY AND GET YOUR OWN SOUVENIR PHOTO!");
        setHorizontalComponentAlignment(Alignment.CENTER, labelWelcome);

        ProgressBar progressBarMain = new ProgressBar();
        progressBarMain.setValue(1);
        add(labelWelcome, progressBarMain);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();
        VerticalLayout verticalLayout = new VerticalLayout();

        Image image = new Image("https://maps.googleapis.com/maps/api/staticmap?center=Krakow,Sukiennice&zoom=15&size=1300x2500&key="
                + googlePlacesClient.getGoogleKey(), "KRAKOW map");

        Label labelPlace = new Label("What would you like to find in Krakow?");
        Label labelRefreshPage = new Label("⟳ Please, refresh the page before a new search :)");
        TextField textField = new TextField();

        Label labelRadius = new Label("How far from the city centre? [m]");
        ComboBox<String> comboBoxRadius = new ComboBox<>();
        comboBoxRadius.setItems("100", "200", "300");
        comboBoxRadius.setClearButtonVisible(true);

        Grid<Result> grid = new Grid<>(Result.class);
        grid.removeAllColumns();

        Button buttonSearch = new Button("Search");
        buttonSearch.addClickListener(clickEvent -> {
            googlePlacesClient.setName(textField.getValue());
            googlePlacesClient.setRadius(comboBoxRadius.getValue());

            List<Result> results = googlePlacesClient.getGoogleMapPlaces();
            grid.setItems(results);
            grid.addColumn(r -> r.getName()).setHeader("Name").setWidth("290px");
            grid.addColumn(r -> r.getRating()).setHeader("Rating").setAutoWidth(true);
            grid.addColumn(r -> r.getVicinity()).setHeader("Address").setWidth("300px");

            grid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
                    GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

            if (googlePlacesClient.getGoogleMapPlaces().isEmpty()){
                Dialog dialog = new Dialog();
                dialog.add(new Text("SORRY, THERE ARE NO RESULTS, TRY AGAIN ⮕ "),
                        new Button("Refresh page", e -> UI.getCurrent().getPage().reload()));
                dialog.setWidth("550px");
                dialog.setHeight("100px");
                dialog.open();
            }

            Map<String, String> coordinates = results.stream()
                    .collect(Collectors.toMap(r -> r.getGeometry().getLocation().getLat().toString(),
                            r -> r.getGeometry().getLocation().getLng().toString()));

            String markers = "color:red|" + Joiner.on("|").withKeyValueSeparator(",").join(coordinates) + "|";

            Image imageWithMarkers = new Image((googleMyResultsMapCreator.getGoogleMyResultsMapFinalUrl(markers).toString()),
                    "KRAKOW map");

            horizontalLayout.remove(image, verticalLayout);
            horizontalLayout.add(imageWithMarkers, verticalLayout);
        });

        horizontalLayout.add(image, verticalLayout);
        verticalLayout.add(labelPlace, labelRefreshPage, textField, labelRadius, comboBoxRadius, buttonSearch, grid);
        add(horizontalLayout);

        ProgressBar progressBar = new ProgressBar();
        progressBar.setValue(1);

        Label labelPhotoUrl = new Label("GET YOUR OWN SOUVENIR PHOTO FROM KRAKOW!");
        setHorizontalComponentAlignment(Alignment.CENTER, labelPhotoUrl);
        TextField textFieldPhotoUrl = new TextField("Link to your photo (16:9 will be the best format of photo):");
        textFieldPhotoUrl.setValue("https://cdn.pixabay.com/photo/2019/06/18/04/49/wax-figure-4281412_960_720.jpg");
        textFieldPhotoUrl.setWidthFull();
        Button buttonPhoto = new Button("Click here and wait a moment, the photo will appear below ⬇ :");
        Label labelPhotoUrl1 = new Label("* The photo of Queen Elizabeth II is a photo of a wax figure from the free photo database");
        Label labelPhotoUrl2 = new Label("** The amount of photo transformations is limited by the amount of credits, so it won't work forever :(");
        add(progressBar, labelPhotoUrl, textFieldPhotoUrl, buttonPhoto, labelPhotoUrl1, labelPhotoUrl2);

        buttonPhoto.addClickListener(clickEvent -> {
            try{
                Image imageFromKrakow = new Image(imageClient.getPhotoFromKrakow(textFieldPhotoUrl.getValue()),
                        imageClient.getPhotoFromKrakow(textFieldPhotoUrl.getValue()));
                setHorizontalComponentAlignment(Alignment.CENTER, imageFromKrakow);
                add(imageFromKrakow);
            }catch (Exception e){
                Dialog dialog = new Dialog();
                dialog.add(new Text("OOPS, SOMETHING WENT WRONG, TRY AGAIN ⮕ "),
                        new Button("Refresh page", b -> UI.getCurrent().getPage().reload()));
                dialog.setWidth("560px");
                dialog.setHeight("100px");
                dialog.open();
            }
        });
    }
}
