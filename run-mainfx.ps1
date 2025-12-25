$fxVersion = "21.0.4"
$repo = "$env:USERPROFILE\.m2\repository\org\openjfx"
$mp = "$repo\javafx-controls\$fxVersion\javafx-controls-$fxVersion-win.jar;" +
      "$repo\javafx-fxml\$fxVersion\javafx-fxml-$fxVersion-win.jar;" +
      "$repo\javafx-graphics\$fxVersion\javafx-graphics-$fxVersion-win.jar;" +
      "$repo\javafx-base\$fxVersion\javafx-base-$fxVersion-win.jar"

# Chemin des classes compilées et du driver SQLite
$cp = "target\classes;$env:USERPROFILE\.m2\repository\org\xerial\sqlite-jdbc\3.46.1.3\sqlite-jdbc-3.46.1.3.jar"

# Lancement JavaFX avec module-path + add-modules requis
java --module-path "$mp" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base -cp "$cp" ui.MainFX
