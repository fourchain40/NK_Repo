setwd("C:\\Users\\puckh\\Desktop\\Brice Documents\\DS 2003")
lego<-read.csv("sets.csv", header=TRUE)

# Packages
library(ggplot2)
library(dplyr)
library(plotly)
library(shiny)
library(shinythemes)
library(ggwordcloud)
library(viridis)

# Dropping price columns and subtheme due to lack of data
lego <- select(lego, -c(6,15,16,17))

# Replace NA's with 0 in Minifigures column
lego$Minifigures <- lego$Minifigures %>% replace(is.na(.), 0)

# Cleaning data: changing a few columns from characters to factors
lego[,c(4,5,6,7,9)] <- lapply(lego[,c(4,5,6,7,9)], as.factor)

# Remove all NAs in numeric columns
lego<-lego%>%
  filter(!is.na(Owned))%>%
  filter(!is.na(Pieces))%>%
  filter(!is.na(Rating))

# Top Themes
top_themes<-lego%>%
  group_by(Theme)%>%
  summarise(total_owned = sum(Owned, na.rm = TRUE))%>%
  top_n(20, total_owned) %>% # source
  arrange(desc(total_owned)) %>%
  pull(Theme)


ui<-fluidPage(theme=shinytheme("united"),
              navbarPage("Visualizing LEGO Data",
                         tabPanel("Introduction", titlePanel("Communicating with Data Final Project"),
                                  mainPanel(fluidRow("This project answers three original research questions about our data set on trends in lego set sales.
                                          The data set orginally comes from kaggle and we suited it to our project through several transformations.
                                          First, we removed the 'price' and 'subtheme' columns due to lack of data. Then, for workability, we 
                                          changed several character variables to factors for later analysis. Finally we kept only the top 20
                                          themes to remove neglible groups."),
                                            fluidRow(" Our research questions are unique and hope to cover major conclusions from several areas of the data set:
                                         "),
                                            fluidRow(" 1) How Has LEGO Set Theme Changed Over the Years?"),
                                            fluidRow(" 2) How does ownership for the top 20 themes differ by the numeric variables?"),
                                            fluidRow(" 3) How was the Manufacturing Process of LEGO
                                          Products Changed Over the Years?"),
                                  )
                         ),
                         tabPanel("Theme Wordcloud", titlePanel("How Has LEGO Set Theme Changed Over the Years?"),
                                  sidebarPanel(
                                    sliderInput(
                                      inputId = "year",
                                      label = "Year:",
                                      min = 1975,
                                      max = 2023,
                                      value = 1975,
                                      animate = TRUE,
                                      sep = "",
                                      step = 1
                                    ),
                                    selectInput(
                                      inputId = "cat",
                                      label = "Select Categorical Variable:",
                                      choices = c("Theme", "Theme_Group", "Category"),
                                      selected = "Theme"
                                    ),
                                    selectInput(
                                      inputId = "viridis",
                                      label = "Pick Viridis Color Scale:",
                                      choices = c("magma","inferno","turbo","viridis","rocket","mako","civids","plasma"),
                                      selected = "turbo"
                                    ),
                                    numericInput(
                                      inputId = "size",
                                      label = "Maximum Size:",
                                      min = 10,
                                      max = 50,
                                      value = 25,
                                      step = 1
                                    )
                                  ),
                                  mainPanel(plotOutput("themewordcloud"),"This wordcloud shows all the different themes of LEGO sets in
                                  the selected year between 1975 and 2023. The size of the word 
                                  is proportional to the number of sets with that theme. By looking 
                                  at the data, it can be seen that the overall number of LEGO sets 
                                  available for purchase throughout the years has increased, but 
                                  also the themes of sets available have become significantly more 
                                              complex in nature.")
                                  
                         ),
                         tabPanel("Breakdown by Theme",
                                  sidebarPanel(
                                    selectInput("x","Select X Axis:",
                                                choices = c("Pieces","Minifigures","Rating"),
                                                selected = "Rating"),
                                    selectInput("size2","Select Size Variable:",
                                                choices = c("Pieces","Minifigures","Rating"),
                                                selected="Pieces"),
                                    checkboxGroupInput(
                                      inputId="theme",
                                      label="Choose the theme",
                                      choices=unique(top_themes),
                                      selected="Star Wars"
                                    )
                                  ),
                                  mainPanel(plotlyOutput("themebreakdown")),
                                  fluidRow("This plotly chart shows the ownership for the top 
                                           20 themes compared to and sized by other numerics in the dataset.")
                         ),
                         tabPanel("Manufacturing Brush Plot",titlePanel("How was the Manufacturing Process of LEGO
                                                           Products Changed Over the Years?"),
                                  sidebarPanel(
                                    selectInput("color","Select Color Variable:",
                                                choices = c("Availability","Packaging"),
                                                selected="Packaging"),
                                    selectInput("size3","Select Size Variable:",
                                                choices = c("Owned","Rating"),
                                                selected="Pieces"),
                                    sliderInput(
                                      inputId = "year1",
                                      label = "Year:",
                                      min = 1975,
                                      max = 2023,
                                      value = 1975,
                                      animate = TRUE,
                                      sep = "",
                                      step = 1
                                    )
                                  ),
                                  mainPanel(plotOutput("graph",brush = "plot_brush"),
                                            verbatimTextOutput("information")),
                                  fluidRow("This scatterplot shows the number of minifigures and pieces of LEGO sets
                                           in the selected year between 1975 and 2023. The size argument can be changed
                                           between the numeric variables of sets owned and rating, and the color can 
                                           represent either how the set is available for purchase or its packaging type.
                                           Looking at the data, the availability has become more complex over the years 
                                           due to the emergence of online shopping, but retail remains the most popular.
                                           Boxed continues to be the most popular form of packaging while the number of 
                                           pieces and minifigures varies greatly between years.")
                         ))
)


server<-function(input, output){
  
  # Reactive Question 1
  lego2<-reactive({
    req(input$year)
    filter(lego, Year %in% input$year)
  })
  
  lego3<-reactive({
    lego2()%>%
      group_by(var=get(input$cat))%>%
      summarize(num=n())
  })
  
  # Reactive Question 2
  legoTheme<-reactive({
    req(input$theme)
    lego2<-lego%>%
      filter(!is.na(Owned))%>%
      filter(Owned != 0)%>%
      filter(!is.na(Pieces))%>%
      filter(!is.na(Rating))%>%
      filter(Rating != 0)
    filter(lego2, Theme %in% input$theme)
  })
  
  # Reactive Question 3
  legoq3<-reactive({
    req(input$year1)
    filter(lego, Year %in% input$year1)
  })
  
  # Output Question 1
  output$themewordcloud<-renderPlot({
    ggplot(lego3(), aes(label=lego3()$var, color=lego3()$var,size=lego3()$num)) + 
      geom_text_wordcloud_area(rm_outside = TRUE) + 
      scale_size_area(max_size = input$size) + 
      scale_color_viridis_d(option = input$viridis)
  })
  
  # Output Question 2
  output$themebreakdown<-renderPlotly({
    ggplotly(ggplot(legoTheme(), aes_string(x=input$x, y="Owned", color="Theme", size=input$size2))+
               geom_point(alpha=0.5))
  })
  
  # Output Question 3
  output$graph<-renderPlot({
    ggplot(legoq3(),aes_string(x="Pieces", y="Minifigures", color=input$color, 
                               size=input$size3))+geom_point(alpha=0.4)
  })
  output$information<-renderPrint({
    brushedPoints(lego,input$plot_brush, xvar = "Pieces", yvar = "Minifigures")
  })
}

shinyApp(ui, server)
