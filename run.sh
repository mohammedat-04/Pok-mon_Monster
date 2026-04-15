#!/usr/bin/env bash
java --module-path "$(pwd)/javafx-sdk-17.0.15/lib" \
     --add-modules javafx.controls,javafx.fxml \
     -jar "$(pwd)/build-jar/MainMenu.jar"
