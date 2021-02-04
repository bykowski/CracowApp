package com.cracowapp.visitcracow.gui;

import com.cracowapp.visitcracow.GoogleMyResultsMapCreator;
import com.cracowapp.visitcracow.GooglePlacesClient;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.jsoup.select.Collector;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Route
public class Gui extends VerticalLayout {

    private GooglePlacesClient googlePlacesClient;
    private GoogleMyResultsMapCreator googleMyResultsMapCreator;

    @Autowired
    public Gui(GooglePlacesClient googlePlacesClient, GoogleMyResultsMapCreator googleMyResultsMapCreator) {
        this.googlePlacesClient = googlePlacesClient;
        this.googleMyResultsMapCreator = googleMyResultsMapCreator;

        Label labelWelcome = new Label("GET YOUR UNIQUE PHOTO FROM KRAKOW AND DISCOVER THE CITY!");
        setHorizontalComponentAlignment(Alignment.CENTER, labelWelcome);
        add(labelWelcome);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();
        VerticalLayout verticalLayout = new VerticalLayout();

        Image image = new Image("https://maps.googleapis.com/maps/api/staticmap?center=Krakow,Sukiennice&zoom=15&size=1300x2500&key="
                + googlePlacesClient.getGoogleKey(), "KRAKOW map");

        Label labelPlace = new Label("What you would you like to find in Krakow?");
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
                dialog.add(new Text("SORRY, THERE ARE NO RESULTS, TRY AGAIN -> "),
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
        verticalLayout.add(labelPlace, textField, labelRadius, comboBoxRadius, buttonSearch, grid);
        add(horizontalLayout);
    }
}
