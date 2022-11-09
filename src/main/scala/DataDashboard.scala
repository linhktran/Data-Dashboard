import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, ListView, TableColumn, TableView, ToggleButton, ToggleGroup}
import javafx.scene.paint.Color
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.layout.{HBox, VBox}
import scalafx.stage.FileChooser

import scala.collection.mutable.Buffer
import io.circe.generic.auto._
import io.circe.parser.decode
import scalafx.collections.ObservableBuffer

import scala.math._
import scalafx.scene.chart.{BarChart, CategoryAxis, NumberAxis, PieChart, ScatterChart, XYChart}
import javafx.geometry.Pos
import javafx.scene.control.ButtonBar.ButtonData
import javafx.scene.control.{ButtonType, Dialog, SelectionMode, Tooltip}
import javafx.scene.layout.{Background, BackgroundFill, Border, BorderStroke, BorderStrokeStyle, BorderWidths, CornerRadii}
import javafx.scene.text.{FontPosture, FontWeight}
import scalafx.beans.property.StringProperty
import scalafx.scene.text.Font
import scalafx.stage.FileChooser.ExtensionFilter
import scalafx.geometry.Insets

import java.io.{File, FileOutputStream, PrintWriter}


object DataDashboard extends JFXApp {
  val calc = new Calculation
  stage = new JFXApp.PrimaryStage {
    title.value = "Data Dashboard"
    val root = new VBox()
    scene = new Scene(root)
    val graphBox = new HBox()
    val loadbutt = new Button("Load File")
    val button = new HBox()
    val datapoints = new VBox()
    button.children = List(loadbutt)
    root.children = List(button)
    val label = new HBox()

    loadbutt.onAction = (e : ActionEvent) => {
      val fileChooser = new FileChooser
      val selectedFile = fileChooser.showOpenDialog(stage)

      if (selectedFile!=null) {
      val text = new Label("")
      text.text = "Open: " + selectedFile
      label.children = text
      if (selectedFile.getName.endsWith(".dd")) {
        root.children = List(button, label, datapoints)
        button.children = List(loadbutt)
        val lines = Buffer[String]()
        val source = scala.io.Source.fromFile(selectedFile)
        for (line <- source.getLines()) {
          lines += line
        }
        source.close()

        if (lines.size == 11) {
        val car = new Data(lines.head,lines(1).toDouble.toString, lines(2).toDouble.toString, lines(3).toDouble.toString, lines(4).toDouble.toString, lines(5).toDouble.toString)
        val ave = new Data("Average", lines(6).toDouble.toString, lines(7).toDouble.toString, lines(8).toDouble.toString, lines(9).toDouble.toString, lines(10).toDouble.toString)
        val data = ObservableBuffer(car, ave)
        val table = new TableView(data)
        table.setPrefHeight(250)
        table.editable = false
        table.setFixedCellSize(50)
        val col1 = new TableColumn[Data, String]("Car")
        col1.setStyle("-fx-alignment: CENTER-LEFT;")
        col1.setPrefWidth(255)
        col1.cellValueFactory = cdf => StringProperty(cdf.value.Car)
        val col2 = new TableColumn[Data, String]("MPG")
        col2.setStyle("-fx-alignment: CENTER;")
        col2.setPrefWidth(255)
        col2.cellValueFactory = cdf => StringProperty(cdf.value.MPG)
        val col3 = new TableColumn[Data, String]("Displacement")
        col3.setPrefWidth(255)
        col3.setStyle("-fx-alignment: CENTER;")
        col3.cellValueFactory = cdf => StringProperty(cdf.value.Displacement)
        val col4 = new TableColumn[Data, String]("Horsepower")
        col4.setStyle("-fx-alignment: CENTER;")
        col4.setPrefWidth(255)
        col4.cellValueFactory = cdf => StringProperty(cdf.value.Horsepower)
        val col5 = new TableColumn[Data, String]("Weight")
        col5.setPrefWidth(255)
        col5.setStyle("-fx-alignment: CENTER;")
        col5.cellValueFactory = cdf => StringProperty(cdf.value.Weight)
        val col6 = new TableColumn[Data, String]("Acceleration")
        col6.setStyle("-fx-alignment: CENTER;")
        col6.setPrefWidth(255)
        col6.cellValueFactory = cdf => StringProperty(cdf.value.Accelearation)
        table.columns ++= List(col1, col2, col3, col4, col5, col6)
        val tablebox = new HBox()
        tablebox.setAlignment(Pos.CENTER)
        tablebox.children = table
        datapoints.setAlignment(Pos.CENTER)

        val x1 = new CategoryAxis()
        x1.tickLabelFontProperty().set(Font.font(9))
        val y1 = new NumberAxis()
        val sp1 = new BarChart[String,Number](x1,y1)
        x1.setLabel("Cars")
        y1.setLabel("Value")
        val s1 = new XYChart.Series[String, Number]()
        s1.setName("MPG")
        s1.getData.add(XYChart.Data(car.Car, car.MPG.toDouble))
        s1.getData.add(XYChart.Data(ave.Car, ave.MPG.toDouble))
        sp1.getData.add(s1)
        for (series <- sp1.getData) {
          for (data <- series.getData) {
            val tooltip = new Tooltip((data.getYValue.intValue).toString)
            Tooltip.install(data.getNode, tooltip)
          }
        }

        val x2 = new CategoryAxis()
        x2.tickLabelFontProperty().set(Font.font(9))
        val y2 = new NumberAxis()
        val sp2 = new BarChart[String,Number](x2,y2)
        x2.setLabel("Cars")
        y2.setLabel("Value")
        val s2 = new XYChart.Series[String, Number]()
        s2.setName("Displacement")
        s2.getData.add(XYChart.Data(car.Car, car.Displacement.toDouble))
        s2.getData.add(XYChart.Data(ave.Car, ave.Displacement.toDouble))
        sp2.getData.add(s2)
        for (series <- sp2.getData) {
          for (data <- series.getData) {
            val tooltip = new Tooltip(data.getYValue.intValue.toString)
            Tooltip.install(data.getNode, tooltip)
          }
        }

        val x3 = new CategoryAxis()
        x3.tickLabelFontProperty().set(Font.font(9))
        val y3 = new NumberAxis()
        val sp3 = new BarChart[String,Number](x3,y3)
        x3.setLabel("Cars")
        y3.setLabel("Value")
        val s3 = new XYChart.Series[String, Number]()
        s3.setName("Horsepower")
        s3.getData.add(XYChart.Data(car.Car, car.Horsepower.toDouble))
        s3.getData.add(XYChart.Data(ave.Car, ave.Horsepower.toDouble))
        sp3.getData.add(s3)
        for (series <- sp3.getData) {
          for (data <- series.getData) {
            val tooltip = new Tooltip(data.getYValue.intValue.toString)
            Tooltip.install(data.getNode, tooltip)
          }
        }

        val x4 = new CategoryAxis()
        x4.tickLabelFontProperty().set(Font.font(9))
        val y4 = new NumberAxis()
        val sp4 = new BarChart[String,Number](x4,y4)
        x4.setLabel("Cars")
        y4.setLabel("Value")
        val s4 = new XYChart.Series[String, Number]()
        s4.setName("Weight")
        s4.getData.add(XYChart.Data(car.Car, car.Weight.toDouble))
        s4.getData.add(XYChart.Data(ave.Car, ave.Weight.toDouble))
        sp4.getData.add(s4)
        for (series <- sp4.getData) {
          for (data <- series.getData) {
            val tooltip = new Tooltip(data.getYValue.intValue.toString)
            Tooltip.install(data.getNode, tooltip)
          }
        }

        val x5 = new CategoryAxis()
        x5.tickLabelFontProperty().set(Font.font(9))
        val y5 = new NumberAxis()
        val sp5 = new BarChart[String,Number](x5,y5)
        x5.setLabel("Cars")
        y5.setLabel("Value")
        val s5 = new XYChart.Series[String, Number]()
        s5.setName("Acceleration")
        s5.getData.add(XYChart.Data(car.Car, car.Accelearation.toDouble))
        s5.getData.add(XYChart.Data(ave.Car, ave.Accelearation.toDouble))
        sp5.getData.add(s5)
        for (series <- sp5.getData) {
          for (data <- series.getData) {
            val tooltip = new Tooltip(data.getYValue.intValue.toString)
            Tooltip.install(data.getNode, tooltip)
          }
        }
        graphBox.setAlignment(Pos.BOTTOM_CENTER)
        graphBox.children = List(sp1, sp2, sp3, sp4, sp5)
        datapoints.children = List(tablebox,graphBox)
        }
      } else if (selectedFile.getName.endsWith("cars.json")) {
      val dataBox = new HBox()
      val cards = new HBox()
      val savebutt = new Button("Save")
      dataBox.children = List(datapoints, cards)
      button.children = List(loadbutt, savebutt)
      root.children = List(button, label, graphBox, dataBox)

      case class Information(Car: String, MPG: String, Cylinders: String, Displacement: String, Horsepower: String, Weight: String, Acceleration: String, Model: String, Origin: String)

      val fileSource   = util.Try(scala.io.Source.fromFile(selectedFile))

      def readText(source: scala.io.Source) = source.getLines().mkString("\n")

      def readJson(text: String) = decode[List[Information]](text).toTry
      val cal = new Calculation

      val data = {
        val temp = Buffer[Information]()
        for {
          source <- fileSource     // Get succesful source result
          text   =  readText(source) // Get text from source
          list   <- readJson(text)   // Parse JSON into list
          datum <- list
        } temp += datum

        temp
      }

      val totalCars = data.size

      val buttons = new HBox(100)
      val MPGbut = new ToggleButton("MPG")
      MPGbut.setPrefSize(125, 10)
      val Disbut = new ToggleButton("Displacement")
      Disbut.setPrefSize(125, 10)
      val Horsebut = new ToggleButton("Horsepower")
      Horsebut.setPrefSize(125, 10)
      val Weightbut = new ToggleButton("Weight")
      Weightbut.setPrefSize(125, 10)
      val Accbut = new ToggleButton("Acceleration")
      Accbut.setPrefSize(125, 10)

      val tb = new ToggleGroup()
      MPGbut.setToggleGroup(tb)
      Disbut.setToggleGroup(tb)
      Horsebut.setToggleGroup(tb)
      Weightbut.setToggleGroup(tb)
      Accbut.setToggleGroup(tb)

      buttons.children += MPGbut
      buttons.children += Disbut
      buttons.children += Horsebut
      buttons.children += Weightbut
      buttons.children += Accbut

      val font1 = Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 25)
      val font2 = Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 12)

      val stats = new HBox(100)
      MPGbut.onAction = (e: ActionEvent) => {
      val min = new VBox()
      min.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      min.setBackground(new Background(new BackgroundFill(Color.CORAL, CornerRadii.EMPTY,Insets.Empty)))
      min.setAlignment(Pos.CENTER)
      min.setPrefSize(125, 100)
      val mintext1 = new Label("")
      val mintext2 = new Label("")
      mintext1.text = "Minimum"
      mintext2.text = round(cal.min(data.map(_.MPG.toDouble).toArray)).toString
      mintext1.setFont(font2)
      mintext2.setFont(font1)
      min.children = List(mintext2, mintext1)
      val max = new VBox()
      max.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      max.setBackground(new Background(new BackgroundFill(Color.CORAL, CornerRadii.EMPTY,Insets.Empty)))
      max.setAlignment(Pos.CENTER)
      max.setPrefSize(125, 100)
      val maxtext1 = new Label("")
      val maxtext2 = new Label("")
      maxtext1.text = "Maximum"
      maxtext2.text = round(cal.max(data.map(_.MPG.toDouble).toArray)).toString
      maxtext1.setFont(font2)
      maxtext2.setFont(font1)
      max.children = List(maxtext2, maxtext1)
      val sum = new VBox()
      sum.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      sum.setBackground(new Background(new BackgroundFill(Color.CORAL, CornerRadii.EMPTY,Insets.Empty)))
      sum.setAlignment(Pos.CENTER)
      sum.setPrefSize(125, 100)
      val sumtext1 = new Label("")
      val sumtext2 = new Label("")
      sumtext1.text = "Sum"
      sumtext2.text = round(cal.sum(data.map(_.MPG.toDouble).toArray)).toString
      sumtext1.setFont(font2)
      sumtext2.setFont(font1)
      sum.children = List(sumtext2, sumtext1)
      val ave = new VBox()
      ave.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      ave.setBackground(new Background(new BackgroundFill(Color.CORAL, CornerRadii.EMPTY,Insets.Empty)))
      ave.setAlignment(Pos.CENTER)
      ave.setPrefSize(125, 100)
      val avetext1 = new Label("")
      val avetext2 = new Label("")
      avetext1.text = "Average"
      avetext2.text = round(cal.average(data.map(_.MPG.toDouble).toArray)).toString
      avetext1.setFont(font2)
      avetext2.setFont(font1)
      ave.children = List(avetext2, avetext1)
      val sd = new VBox()
      sd.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      sd.setBackground(new Background(new BackgroundFill(Color.CORAL, CornerRadii.EMPTY,Insets.Empty)))
      sd.setAlignment(Pos.CENTER)
      sd.setPrefSize(125, 100)
      val sdtext1 = new Label("")
      val sdtext2 = new Label("")
      sdtext1.text = "Standard Deviation"
      sdtext2.text = round(cal.sd(data.map(_.MPG.toDouble).toArray)).toString
      sdtext1.setFont(font2)
      sdtext2.setFont(font1)
      sd.children = List(sdtext2, sdtext1)
      stats.children = List(min, max, sum, ave, sd)
      }

      Disbut.onAction = (e: ActionEvent) => {
      val min = new VBox()
      min.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      min.setBackground(new Background(new BackgroundFill(Color.CORAL, CornerRadii.EMPTY,Insets.Empty)))
      min.setAlignment(Pos.CENTER)
      min.setPrefSize(125, 100)
      val mintext1 = new Label("")
      val mintext2 = new Label("")
      mintext1.text = "Minimum"
      mintext2.text = round(cal.min(data.map(_.Displacement.toDouble).toArray)).toString
      mintext1.setFont(font2)
      mintext2.setFont(font1)
      min.children = List(mintext2, mintext1)
      val max = new VBox()
      max.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      max.setBackground(new Background(new BackgroundFill(Color.CORAL, CornerRadii.EMPTY,Insets.Empty)))
      max.setAlignment(Pos.CENTER)
      max.setPrefSize(125, 100)
      val maxtext1 = new Label("")
      val maxtext2 = new Label("")
      maxtext1.text = "Maximum"
      maxtext2.text = round(cal.max(data.map(_.Displacement.toDouble).toArray)).toString
      maxtext1.setFont(font2)
      maxtext2.setFont(font1)
      max.children = List(maxtext2, maxtext1)
      val sum = new VBox()
      sum.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      sum.setBackground(new Background(new BackgroundFill(Color.CORAL, CornerRadii.EMPTY,Insets.Empty)))
      sum.setAlignment(Pos.CENTER)
      sum.setPrefSize(125, 100)
      val sumtext1 = new Label("")
      val sumtext2 = new Label("")
      sumtext1.text = "Sum"
      sumtext2.text = round(cal.sum(data.map(_.Displacement.toDouble).toArray)).toString
      sumtext1.setFont(font2)
      sumtext2.setFont(font1)
      sum.children = List(sumtext2, sumtext1)
      val ave = new VBox()
      ave.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      ave.setBackground(new Background(new BackgroundFill(Color.CORAL, CornerRadii.EMPTY,Insets.Empty)))
      ave.setAlignment(Pos.CENTER)
      ave.setPrefSize(125, 100)
      val avetext1 = new Label("")
      val avetext2 = new Label("")
      avetext1.text = "Average"
      avetext2.text = round(cal.average(data.map(_.Displacement.toDouble).toArray)).toString
      avetext1.setFont(font2)
      avetext2.setFont(font1)
      ave.children = List(avetext2, avetext1)
      val sd = new VBox()
      sd.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      sd.setBackground(new Background(new BackgroundFill(Color.CORAL, CornerRadii.EMPTY,Insets.Empty)))
      sd.setAlignment(Pos.CENTER)
      sd.setPrefSize(125, 100)
      val sdtext1 = new Label("")
      val sdtext2 = new Label("")
      sdtext1.text = "Standard Deviation"
      sdtext2.text = round(cal.sd(data.map(_.Displacement.toDouble).toArray)).toString
      sdtext1.setFont(font2)
      sdtext2.setFont(font1)
      sd.children = List(sdtext2, sdtext1)
      stats.children = List(min,max, sum, ave, sd)
      }

      Horsebut.onAction = (e: ActionEvent) => {
      val min = new VBox()
      min.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      min.setBackground(new Background(new BackgroundFill(Color.CORAL, CornerRadii.EMPTY,Insets.Empty)))
      min.setAlignment(Pos.CENTER)
      min.setPrefSize(125, 100)
      val mintext1 = new Label("")
      val mintext2 = new Label("")
      mintext1.text = "Minimum"
      mintext2.text = round(cal.min(data.map(_.Horsepower.toDouble).toArray)).toString
      mintext1.setFont(font2)
      mintext2.setFont(font1)
      min.children = List(mintext2, mintext1)
      val max = new VBox()
      max.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      max.setBackground(new Background(new BackgroundFill(Color.CORAL, CornerRadii.EMPTY,Insets.Empty)))
      max.setAlignment(Pos.CENTER)
      max.setPrefSize(125, 100)
      val maxtext1 = new Label("")
      val maxtext2 = new Label("")
      maxtext1.text = "Maximum"
      maxtext2.text = round(cal.max(data.map(_.Horsepower.toDouble).toArray)).toString
      maxtext1.setFont(font2)
      maxtext2.setFont(font1)
      max.children = List(maxtext2, maxtext1)
      val sum = new VBox()
      sum.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      sum.setBackground(new Background(new BackgroundFill(Color.CORAL, CornerRadii.EMPTY,Insets.Empty)))
      sum.setAlignment(Pos.CENTER)
      sum.setPrefSize(125, 100)
      val sumtext1 = new Label("")
      val sumtext2 = new Label("")
      sumtext1.text = "Sum"
      sumtext2.text = round(cal.sum(data.map(_.Horsepower.toDouble).toArray)).toString
      sumtext1.setFont(font2)
      sumtext2.setFont(font1)
      sum.children = List(sumtext2, sumtext1)
      val ave = new VBox()
      ave.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      ave.setBackground(new Background(new BackgroundFill(Color.CORAL, CornerRadii.EMPTY,Insets.Empty)))
      ave.setAlignment(Pos.CENTER)
      ave.setPrefSize(125, 100)
      val avetext1 = new Label("")
      val avetext2 = new Label("")
      avetext1.text = "Average"
      avetext2.text = round(cal.average(data.map(_.Horsepower.toDouble).toArray)).toString
      avetext1.setFont(font2)
      avetext2.setFont(font1)
      ave.children = List(avetext2, avetext1)
      val sd = new VBox()
      sd.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      sd.setBackground(new Background(new BackgroundFill(Color.CORAL, CornerRadii.EMPTY,Insets.Empty)))
      sd.setAlignment(Pos.CENTER)
      sd.setPrefSize(125, 100)
      val sdtext1 = new Label("")
      val sdtext2 = new Label("")
      sdtext1.text = "Standard Deviation"
      sdtext2.text = round(cal.sd(data.map(_.Horsepower.toDouble).toArray)).toString
      sdtext1.setFont(font2)
      sdtext2.setFont(font1)
      sd.children = List(sdtext2, sdtext1)
      stats.children = List(min, max, sum, ave, sd)
      }

      Weightbut.onAction = (e: ActionEvent) => {
      val min = new VBox()
      min.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      min.setBackground(new Background(new BackgroundFill(Color.CORAL, CornerRadii.EMPTY,Insets.Empty)))
      min.setAlignment(Pos.CENTER)
      min.setPrefSize(125, 100)
      val mintext1 = new Label("")
      val mintext2 = new Label("")
      mintext1.text = "Minimum"
      mintext2.text = round(cal.min(data.map(_.Weight.toDouble).toArray)).toString
      mintext1.setFont(font2)
      mintext2.setFont(font1)
      min.children = List(mintext2, mintext1)
      val max = new VBox()
      max.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      max.setBackground(new Background(new BackgroundFill(Color.CORAL, CornerRadii.EMPTY,Insets.Empty)))
      max.setAlignment(Pos.CENTER)
      max.setPrefSize(125, 100)
      val maxtext1 = new Label("")
      val maxtext2 = new Label("")
      maxtext1.text = "Maximum"
      maxtext2.text = round(cal.max(data.map(_.Weight.toDouble).toArray)).toString
      maxtext1.setFont(font2)
      maxtext2.setFont(font1)
      max.children = List(maxtext2, maxtext1)
      val sum = new VBox()
      sum.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      sum.setBackground(new Background(new BackgroundFill(Color.CORAL, CornerRadii.EMPTY,Insets.Empty)))
      sum.setAlignment(Pos.CENTER)
      sum.setPrefSize(125, 100)
      val sumtext1 = new Label("")
      val sumtext2 = new Label("")
      sumtext1.text = "Sum"
      sumtext2.text = round(cal.sum(data.map(_.Weight.toDouble).toArray)).toString
      sumtext1.setFont(font2)
      sumtext2.setFont(font1)
      sum.children = List(sumtext2, sumtext1)
      val ave = new VBox()
      ave.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      ave.setBackground(new Background(new BackgroundFill(Color.CORAL, CornerRadii.EMPTY,Insets.Empty)))
      ave.setAlignment(Pos.CENTER)
      ave.setPrefSize(125, 100)
      val avetext1 = new Label("")
      val avetext2 = new Label("")
      avetext1.text = "Average"
      avetext2.text = round(cal.average(data.map(_.Weight.toDouble).toArray)).toString
      avetext1.setFont(font2)
      avetext2.setFont(font1)
      ave.children = List(avetext2, avetext1)
      val sd = new VBox()
      sd.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      sd.setBackground(new Background(new BackgroundFill(Color.CORAL, CornerRadii.EMPTY,Insets.Empty)))
      sd.setAlignment(Pos.CENTER)
      sd.setPrefSize(125, 100)
      val sdtext1 = new Label("")
      val sdtext2 = new Label("")
      sdtext1.text = "Standard Deviation"
      sdtext2.text = round(cal.sd(data.map(_.Weight.toDouble).toArray)).toString
      sdtext1.setFont(font2)
      sdtext2.setFont(font1)
      sd.children = List(sdtext2, sdtext1)
      stats.children = List(min, max, sum, ave, sd)
      }

      Accbut.onAction = (e: ActionEvent) => {
      val min = new VBox()
      min.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      min.setBackground(new Background(new BackgroundFill(Color.CORAL, CornerRadii.EMPTY,Insets.Empty)))
      min.setAlignment(Pos.CENTER)
      min.setPrefSize(125, 100)
      val mintext1 = new Label("")
      val mintext2 = new Label("")
      mintext1.text = "Minimum"
      mintext2.text = round(cal.min(data.map(_.Acceleration.toDouble).toArray)).toString
      mintext1.setFont(font2)
      mintext2.setFont(font1)
      min.children = List(mintext2, mintext1)
      val max = new VBox()
      max.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      max.setBackground(new Background(new BackgroundFill(Color.CORAL, CornerRadii.EMPTY,Insets.Empty)))
      max.setAlignment(Pos.CENTER)
      max.setPrefSize(125, 100)
      val maxtext1 = new Label("")
      val maxtext2 = new Label("")
      maxtext1.text = "Maximum"
      maxtext2.text = round(cal.max(data.map(_.Acceleration.toDouble).toArray)).toString
      maxtext1.setFont(font2)
      maxtext2.setFont(font1)
      max.children = List(maxtext2, maxtext1)
      val sum = new VBox()
      sum.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      sum.setBackground(new Background(new BackgroundFill(Color.CORAL, CornerRadii.EMPTY,Insets.Empty)))
      sum.setAlignment(Pos.CENTER)
      sum.setPrefSize(125, 100)
      val sumtext1 = new Label("")
      val sumtext2 = new Label("")
      sumtext1.text = "Sum"
      sumtext2.text = round(cal.sum(data.map(_.Acceleration.toDouble).toArray)).toString
      sumtext1.setFont(font2)
      sumtext2.setFont(font1)
      sum.children = List(sumtext2, sumtext1)
      val ave = new VBox()
      ave.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      ave.setBackground(new Background(new BackgroundFill(Color.CORAL, CornerRadii.EMPTY,Insets.Empty)))
      ave.setAlignment(Pos.CENTER)
      ave.setPrefSize(125, 100)
      val avetext1 = new Label("")
      val avetext2 = new Label("")
      avetext1.text = "Average"
      avetext2.text = round(cal.average(data.map(_.Acceleration.toDouble).toArray)).toString
      avetext1.setFont(font2)
      avetext2.setFont(font1)
      ave.children = List(avetext2, avetext1)
      val sd = new VBox()
      sd.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      sd.setBackground(new Background(new BackgroundFill(Color.CORAL, CornerRadii.EMPTY,Insets.Empty)))
      sd.setAlignment(Pos.CENTER)
      sd.setPrefSize(125, 100)
      val sdtext1 = new Label("")
      val sdtext2 = new Label("")
      sdtext1.text = "Standard Deviation"
      sdtext2.text = round(cal.sd(data.map(_.Acceleration.toDouble).toArray)).toString
      sdtext1.setFont(font2)
      sdtext2.setFont(font1)
      sd.children = List(sdtext2, sdtext1)
      stats.children = List(min, max, sum, ave, sd)
      }

      val combined = new VBox(30)
      combined.children += buttons
      combined.children += stats

      val total = new VBox(15)
      val font3 = Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18)
      val font4 = Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 12)
      val US = new VBox()
      US.setBackground(new Background(new BackgroundFill(Color.TURQUOISE, CornerRadii.EMPTY,Insets.Empty)))
      US.setAlignment(Pos.CENTER)
      US.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      US.setPrefSize(100, 50)
      val UStext1 = new Label("")
      val UStext2 = new Label("")
      UStext1.text = "US cars"
      UStext1.setFont(font4)
      UStext2.text = data.count(_.Origin == "US").toString
      UStext2.setFont(font3)
      US.children = List(UStext2, UStext1)
      val EU = new VBox()
      EU.setBackground(new Background(new BackgroundFill(Color.TURQUOISE, CornerRadii.EMPTY,Insets.Empty)))
      EU.setAlignment(Pos.CENTER)
      EU.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      EU.setPrefSize(100, 50)
      val EUtext1 = new Label("")
      val EUtext2 = new Label("")
      EUtext1.text = "EU cars"
      EUtext1.setFont(font4)
      EUtext2.text = data.count(_.Origin == "Europe").toString
      EUtext2.setFont(font3)
      EU.children = List(EUtext2, EUtext1)
      val Jap = new VBox()
      Jap.setBackground(new Background(new BackgroundFill(Color.TURQUOISE, CornerRadii.EMPTY,Insets.Empty)))
      Jap.setAlignment(Pos.CENTER)
      Jap.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)))
      Jap.setPrefSize(100, 50)
      val Jtext1 = new Label("")
      val Jtext2 = new Label("")
      Jtext1.text = "Japan cars"
      Jtext1.setFont(font4)
      Jtext2.text = data.count(_.Origin == "Japan").toString
      Jtext2.setFont(font3)
      Jap.children = List(Jtext2, Jtext1)
      total.children = List(US, EU, Jap)

      val graphslot1 = new VBox()
      graphslot1.setPrefSize(850, 350)
      val graphslot2 = new VBox()
      graphslot2.setPrefSize(850, 350)
      val graphslot3 = new VBox()
      graphslot3.setPrefSize(850, 350)
      graphBox.children = List(graphslot1, graphslot2, graphslot3)

      val buttons1 = new HBox()
      buttons1.setAlignment(Pos.CENTER)
      val barbutt1 = new ToggleButton("Bar Chart")
      val scattbutt1 = new ToggleButton("Scatter Plot")
      val piebutt1 = new ToggleButton("Pie Chart")
      val hidebutt1 = new ToggleButton("Hide")
      val tb1 = new ToggleGroup()
      barbutt1.setToggleGroup(tb1)
      scattbutt1.setToggleGroup(tb1)
      piebutt1.setToggleGroup(tb1)
      hidebutt1.setToggleGroup(tb1)
      val graph1 = new VBox()
      buttons1.children = List(barbutt1, scattbutt1, piebutt1, hidebutt1)
      graphslot1.children = List(buttons1, graph1)
      barbutt1.onAction = (e: ActionEvent) => {
      val xAxis1 = new CategoryAxis()
      val yAxis1 = new NumberAxis()
      val bc = new BarChart[String,Number](xAxis1,yAxis1)
      xAxis1.setLabel("Origin")
      yAxis1.setLabel("Value")

      val series1 = new XYChart.Series[String, Number]()
      series1.setName("Quantity")
      series1.getData.add(XYChart.Data("US", data.count(_.Origin == "US")))
      series1.getData.add(XYChart.Data("Europe", data.count(_.Origin == "Europe")))
      series1.getData.add(XYChart.Data("Japan", data.count(_.Origin == "Japan")))
      bc.getData.add(series1)
      for (series <- bc.getData) {
        for (data <- series.getData) {
          val tooltip = new Tooltip(data.getXValue + ": " + data.getYValue.toString)
          Tooltip.install(data.getNode, tooltip)
        }
      }
        graph1.children = bc
      }
      scattbutt1.onAction = (e: ActionEvent) => {
      val xAxis2 = new CategoryAxis()
      val yAxis2 = new NumberAxis()
      val sp = new ScatterChart[String,Number](xAxis2,yAxis2)
      xAxis2.setLabel("Origin")
      yAxis2.setLabel("Value")

      val series2 = new XYChart.Series[String, Number]()
      series2.setName("Quantity")
      series2.getData.add(XYChart.Data("US", data.count(_.Origin == "US")))
      series2.getData.add(XYChart.Data("Europe", data.count(_.Origin == "Europe")))
      series2.getData.add(XYChart.Data("Japan", data.count(_.Origin == "Japan")))
      sp.getData.add(series2)
      for (series <- sp.getData) {
        for (data <- series.getData) {
          val tooltip = new Tooltip(data.getXValue + ": " + data.getYValue.toString)
          Tooltip.install(data.getNode, tooltip)
        }
      }
        graph1.children = sp
      }
      piebutt1.onAction = (e: ActionEvent) => {
      val pcList = ObservableBuffer(PieChart.Data("US", data.count(_.Origin == "US")), PieChart.Data("Europe", data.count(_.Origin == "Europe")), PieChart.Data("Japan", data.count(_.Origin == "Japan")))
      val pc = new PieChart(pcList)
      for (data <- pc.getData) {
        val value = data.getPieValue
        val percent = value/totalCars * 100.0
        val tooltip = new Tooltip(data.getName + ": " + value.toInt.toString + " - " + round(percent) + "%")
        Tooltip.install(data.getNode, tooltip)
      }
        graph1.children = pc
      }
      hidebutt1.onAction = (e: ActionEvent) => {
        graph1.children = List()
      }

      val buttons2 = new HBox()
      buttons2.setAlignment(Pos.CENTER)
      val barbutt2 = new ToggleButton("Bar Chart")
      val scattbutt2 = new ToggleButton("Scatter Plot")
      val piebutt2 = new ToggleButton("Pie Chart")
      val hidebutt2 = new ToggleButton("Hide")
      val tb2 = new ToggleGroup()
      barbutt2.setToggleGroup(tb2)
      scattbutt2.setToggleGroup(tb2)
      piebutt2.setToggleGroup(tb2)
      hidebutt2.setToggleGroup(tb2)
      val graph2 = new VBox()
      buttons2.children = List(barbutt2, scattbutt2, piebutt2, hidebutt2)
      graphslot2.children = List(buttons2, graph2)
      barbutt2.onAction = (e: ActionEvent) => {
      val xAxis1 = new CategoryAxis()
      val yAxis1 = new NumberAxis()
      val bc = new BarChart[String,Number](xAxis1,yAxis1)
      xAxis1.setLabel("Origin")
      yAxis1.setLabel("Value")

      val series1 = new XYChart.Series[String, Number]()
      series1.setName("Quantity")
      series1.getData.add(XYChart.Data("US", data.count(_.Origin == "US")))
      series1.getData.add(XYChart.Data("Europe", data.count(_.Origin == "Europe")))
      series1.getData.add(XYChart.Data("Japan", data.count(_.Origin == "Japan")))
      bc.getData.add(series1)
      for (series <- bc.getData) {
        for (data <- series.getData) {
          val tooltip = new Tooltip(data.getXValue + ": " + data.getYValue.toString)
          Tooltip.install(data.getNode, tooltip)
        }
      }
        graph2.children = bc
      }
      scattbutt2.onAction = (e: ActionEvent) => {
      val xAxis2 = new CategoryAxis()
      val yAxis2 = new NumberAxis()
      val sp = new ScatterChart[String,Number](xAxis2,yAxis2)
      xAxis2.setLabel("Origin")
      yAxis2.setLabel("Value")

      val series2 = new XYChart.Series[String, Number]()
      series2.setName("Quantity")
      series2.getData.add(XYChart.Data("US", data.count(_.Origin == "US")))
      series2.getData.add(XYChart.Data("Europe", data.count(_.Origin == "Europe")))
      series2.getData.add(XYChart.Data("Japan", data.count(_.Origin == "Japan")))
      sp.getData.add(series2)
      for (series <- sp.getData) {
        for (data <- series.getData) {
          val tooltip = new Tooltip(data.getXValue + ": " + data.getYValue.toString)
          Tooltip.install(data.getNode, tooltip)
        }
      }
        graph2.children = sp
      }
      piebutt2.onAction = (e: ActionEvent) => {
      val pcList = ObservableBuffer(PieChart.Data("US", data.count(_.Origin == "US")), PieChart.Data("Europe", data.count(_.Origin == "Europe")), PieChart.Data("Japan", data.count(_.Origin == "Japan")))
      val pc = new PieChart(pcList)
      for (data <- pc.getData) {
        val value = data.getPieValue
        val percent = value/totalCars * 100.0
        val tooltip = new Tooltip(data.getName + ": " + value.toInt.toString + " - " + round(percent) + "%")
        Tooltip.install(data.getNode, tooltip)
      }
        graph2.children = pc
      }
      hidebutt2.onAction = (e: ActionEvent) => {
        graph2.children = List()
      }

      val buttons3 = new HBox()
      buttons3.setAlignment(Pos.CENTER)
      val barbutt3 = new ToggleButton("Bar Chart")
      val scattbutt3 = new ToggleButton("Scatter Plot")
      val piebutt3 = new ToggleButton("Pie Chart")
      val hidebutt3 = new ToggleButton("Hide")
      val tb3 = new ToggleGroup()
      barbutt3.setToggleGroup(tb3)
      scattbutt3.setToggleGroup(tb3)
      piebutt3.setToggleGroup(tb3)
      hidebutt3.setToggleGroup(tb3)
      val graph3 = new VBox()
      buttons3.children = List(barbutt3, scattbutt3, piebutt3, hidebutt3)
      graphslot3.children = List(buttons3, graph3)
      barbutt3.onAction = (e: ActionEvent) => {
      val xAxis1 = new CategoryAxis()
      val yAxis1 = new NumberAxis()
      val bc = new BarChart[String,Number](xAxis1,yAxis1)
      xAxis1.setLabel("Origin")
      yAxis1.setLabel("Value")

      val series1 = new XYChart.Series[String, Number]()
      series1.setName("Quantity")
      series1.getData.add(XYChart.Data("US", data.count(_.Origin == "US")))
      series1.getData.add(XYChart.Data("Europe", data.count(_.Origin == "Europe")))
      series1.getData.add(XYChart.Data("Japan", data.count(_.Origin == "Japan")))
      bc.getData.add(series1)
      for (series <- bc.getData) {
        for (data <- series.getData) {
          val tooltip = new Tooltip(data.getXValue + ": " + data.getYValue.toString)
          Tooltip.install(data.getNode, tooltip)
        }
      }
        graph3.children = bc
      }
      scattbutt3.onAction = (e: ActionEvent) => {
      val xAxis2 = new CategoryAxis()
      val yAxis2 = new NumberAxis()
      val sp = new ScatterChart[String,Number](xAxis2,yAxis2)
      xAxis2.setLabel("Origin")
      yAxis2.setLabel("Value")

      val series2 = new XYChart.Series[String, Number]()
      series2.setName("Quantity")
      series2.getData.add(XYChart.Data("US", data.count(_.Origin == "US")))
      series2.getData.add(XYChart.Data("Europe", data.count(_.Origin == "Europe")))
      series2.getData.add(XYChart.Data("Japan", data.count(_.Origin == "Japan")))
      sp.getData.add(series2)
      for (series <- sp.getData) {
        for (data <- series.getData) {
          val tooltip = new Tooltip(data.getXValue + ": " + data.getYValue.toString)
          Tooltip.install(data.getNode, tooltip)
        }
      }
        graph3.children = sp
      }
      piebutt3.onAction = (e: ActionEvent) => {
      val pcList = ObservableBuffer(PieChart.Data("US", data.count(_.Origin == "US")), PieChart.Data("Europe", data.count(_.Origin == "Europe")), PieChart.Data("Japan", data.count(_.Origin == "Japan")))
      val pc = new PieChart(pcList)
      for (data <- pc.getData) {
        val value = data.getPieValue
        val percent = value/totalCars * 100.0
        val tooltip = new Tooltip(data.getName + ": " + value.toInt.toString + " - " + round(percent) + "%")
        Tooltip.install(data.getNode, tooltip)
      }
        graph3.children = pc
      }
      hidebutt3.onAction = (e: ActionEvent) => {
        graph3.children = List()
      }

      val choose = new HBox(20)
      choose.setPadding(Insets(20,20,20,20))
      val chooseButt = new Button("Choose")
      val carsBuffer = new ObservableBuffer[String]()
      for (n <- data.indices) {
        carsBuffer += data.map(_.Car)(n)
      }
      val listview = new ListView[String](carsBuffer.sorted)
      listview.getSelectionModel.setSelectionMode(SelectionMode.SINGLE)
      listview.setPrefSize(200, 100)
      choose.children = List(listview, chooseButt, total, combined)

      val info = Buffer[Information]()

      def buttonClicked() = {
        var car = ""
        car = listview.getSelectionModel.getSelectedItem
        val graphs = new HBox(0)

        if (car == null) {
          val dialog = new Dialog[String]()
          dialog.setTitle("Warning!")
          val butttype = new ButtonType("OK", ButtonData.OK_DONE)
          dialog.setContentText("You need to choose a car first!")
          dialog.getDialogPane.getButtonTypes.add(butttype)
          dialog.showAndWait()
          new HBox()
        } else {
        if (info.nonEmpty) info(0) = data.filter(_.Car == car).head else info += data.filter(_.Car == car).head
        val x1 = new CategoryAxis()
        x1.tickLabelFontProperty().set(Font.font(9))
        val y1 = new NumberAxis()
        val sp1 = new ScatterChart[String,Number](x1,y1)
        x1.setLabel("Cars")
        y1.setLabel("Value")
        val s1 = new XYChart.Series[String, Number]()
        s1.setName("MPG")
        s1.getData.add(XYChart.Data(car, data.filter(_.Car == car).map(_.MPG.toDouble).head))
        s1.getData.add(XYChart.Data("Average", cal.average(data.map(_.MPG.toDouble).toArray)))
        sp1.getData.add(s1)
        for (series <- sp1.getData) {
          for (data <- series.getData) {
            val tooltip = new Tooltip((data.getYValue.intValue).toString)
            Tooltip.install(data.getNode, tooltip)
          }
        }

        val x2 = new CategoryAxis()
        x2.tickLabelFontProperty().set(Font.font(9))
        val y2 = new NumberAxis()
        val sp2 = new ScatterChart[String,Number](x2,y2)
        x2.setLabel("Cars")
        y2.setLabel("Value")
        val s2 = new XYChart.Series[String, Number]()
        s2.setName("Displacement")
        s2.getData.add(XYChart.Data(car, data.filter(_.Car == car).map(_.Displacement.toDouble).head))
        s2.getData.add(XYChart.Data("Average", cal.average(data.map(_.Displacement.toDouble).toArray)))
        sp2.getData.add(s2)
        for (series <- sp2.getData) {
          for (data <- series.getData) {
            val tooltip = new Tooltip(data.getYValue.intValue.toString)
            Tooltip.install(data.getNode, tooltip)
          }
        }

        val x3 = new CategoryAxis()
        x3.tickLabelFontProperty().set(Font.font(9))
        val y3 = new NumberAxis()
        val sp3 = new ScatterChart[String,Number](x3,y3)
        x3.setLabel("Cars")
        y3.setLabel("Value")
        val s3 = new XYChart.Series[String, Number]()
        s3.setName("Horsepower")
        s3.getData.add(XYChart.Data(car, data.filter(_.Car == car).map(_.Horsepower.toDouble).head))
        s3.getData.add(XYChart.Data("Average", cal.average(data.map(_.Horsepower.toDouble).toArray)))
        sp3.getData.add(s3)
        for (series <- sp3.getData) {
          for (data <- series.getData) {
            val tooltip = new Tooltip(data.getYValue.intValue.toString)
            Tooltip.install(data.getNode, tooltip)
          }
        }

        val x4 = new CategoryAxis()
        x4.tickLabelFontProperty().set(Font.font(9))
        val y4 = new NumberAxis()
        val sp4 = new ScatterChart[String,Number](x4,y4)
        x4.setLabel("Cars")
        y4.setLabel("Value")
        val s4 = new XYChart.Series[String, Number]()
        s4.setName("Weight")
        s4.getData.add(XYChart.Data(car, data.filter(_.Car == car).map(_.Weight.toDouble).head))
        s4.getData.add(XYChart.Data("Average", cal.average(data.map(_.Weight.toDouble).toArray)))
        sp4.getData.add(s4)
        for (series <- sp4.getData) {
          for (data <- series.getData) {
            val tooltip = new Tooltip(data.getYValue.intValue.toString)
            Tooltip.install(data.getNode, tooltip)
          }
        }

        val x5 = new CategoryAxis()
        x5.tickLabelFontProperty().set(Font.font(9))
        val y5 = new NumberAxis()
        val sp5 = new ScatterChart[String,Number](x5,y5)
        x5.setLabel("Cars")
        y5.setLabel("Value")
        val s5 = new XYChart.Series[String, Number]()
        s5.setName("Acceleration")
        s5.getData.add(XYChart.Data(car, data.filter(_.Car == car).map(_.Acceleration.toDouble).head))
        s5.getData.add(XYChart.Data("Average", cal.average(data.map(_.Acceleration.toDouble).toArray)))
        sp5.getData.add(s5)
        for (series <- sp5.getData) {
          for (data <- series.getData) {
            val tooltip = new Tooltip(data.getYValue.intValue.toString)
            Tooltip.install(data.getNode, tooltip)
          }
        }

        sp1.setPrefSize(750, 250)
        sp2.setPrefSize(750, 250)
        sp3.setPrefSize(750, 250)
        sp4.setPrefSize(750, 250)
        sp5.setPrefSize(750, 250)
        graphs.children = List(sp1, sp2, sp3, sp4, sp5)
        graphs
      }
      }

      savebutt.onAction = (e : ActionEvent) => {
        if (info.isEmpty) {
          val dialog = new Dialog[String]()
          dialog.setTitle("Warning!")
          val butttype = new ButtonType("OK", ButtonData.OK_DONE)
          dialog.setContentText("You need to choose a car first before saving!")
          dialog.getDialogPane.getButtonTypes.add(butttype)
          dialog.showAndWait()
        } else if (info.nonEmpty) {
          val FC = new FileChooser()
          FC.getExtensionFilters.addAll(new ExtensionFilter("Dashboard", "*.dd"))
          val file = FC.showSaveDialog(stage)
          val car = info.head
          val pw = new PrintWriter(new FileOutputStream(new File(file.getCanonicalPath)))
          pw.write(car.Car + "\n" + car.MPG + "\n" + car.Displacement + "\n" + car.Horsepower + "\n" + car.Weight + "\n" + car.Acceleration + "\n" + cal.average(data.map(_.MPG.toDouble).toArray).toInt.toString + "\n"
            + cal.average(data.map(_.Displacement.toDouble).toArray).toInt.toString + "\n" + cal.average(data.map(_.Horsepower.toDouble).toArray).toInt.toString + "\n" + cal.average(data.map(_.Weight.toDouble).toArray).toInt.toString + "\n"
            + cal.average(data.map(_.Acceleration.toDouble).toArray).toInt.toString + "\n")
          pw.close()
        }
      }

      val graph = new HBox()
      chooseButt.onAction = (e: ActionEvent) => {graph.children = buttonClicked()}

      datapoints.children = List(choose, graph)

    }
  }
  }
}
}
