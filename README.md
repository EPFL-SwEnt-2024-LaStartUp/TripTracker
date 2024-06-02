# TripTracker

## Functionalities 

Tired of bringing your heavy guidebook with you on your trips ? Say hello to TripTracker. This application will make it super easy to find relevant places to visit in a city and will enable users to find pleasant walk paths between points of interest (museums, restaurants, nice viewpoints, bars or buildings…) while avoiding crowded or unattractive areas. 
Users who want to share their favorite routes can record their path using their GPS position along the way. They can then add points of interest to describe where to stop as well as some text or pictures to describe them. Before they can share the circuit they will need to add an approximation of the time needed, a title and a short description. Then they can choose to publish the tour with one of the three modes of publishing paths : private, friends only or public. It is also possible to add comments and pictures to registered points of interest and paths. Each recorded path is associated with a rating, this way users can either upvote or downvote them and the best ranked paths are proposed with a higher probability.
The user can select points of interest he wants to see/visit and the desired time. Then the application proposes paths with good rankings corresponding to the criteria and the user just has to follow the GPS. When the user knows that he will be offline during the walk, he can download the path he wants to follow. Each user will have an associated profile and will be able to connect with friends. This way they can easily access each other's recorded paths.
By analyzing the data about the paths recently registered into the application, and the ones that users download and follow, the application will highlight the points of interest that are the most popular in the last week/month and display them in a « trending » tab. This way users will easily see points of interest and paths that might be more relevant currently as it might change depending on the time of the year (based on the seasons, the expositions in museums…).

## Design

**Split app model using public cloud services :**

Data to save for a user are :
- His profile in the application (username, profile picture…)
- Friends the user has connected with in the app
- His total ratings or a global score
  
Other data to save are :
- Paths saved in the application + their rankings
- Points of interest + photos and comments 

**Support multiple users and authentication :**

- Each user must create an account to use the application

**Offline mode: usable without Internet connectivity :**

- Downloading existing paths to access them without Internet
- Recording a new path and upload it later
- Proposing “base guide” for given cities to download and use without internet  

**Use at least one phone sensor as a core feature :**

- Camera : take pictures of interest points along the path, update the profile picture
- GPS : record the path followed and the points of interest, prompt existing paths, analyze popular points of interest and paths

**Other**

- Find friends with an account in the application using real name, login name or email
- Security/privacy: if profile or path is not public, exclude data from the searches 
- When the path record is activated, enters power efficiency mode for low energy consumption, and the app can be left while still working in the background

## Architecture diagram

![architecture_diagram](/resources/architecture_diagram.png)

---


## Figma Project

[Click here to access the Figma prototype](https://www.figma.com/file/mz89h5wBUL7VgwNr0ck3ol/TripTracker?type=design&node-id=0%3A1&mode=design&t=lfFxVepbbW9RYcwM-1) and explore the interactive design.

## Promo Video

Check out our release video to see TripTracker in action and learn more about how it can enhance your travel experience. ![promo video](https://github.com/EPFL-SwEnt-2024-LaStartUp/TripTracker/assets/67064193/5cf0cba2-50b5-4268-8a25-1d0547fa44ea) 

Feel free to navigate through the prototype to get a glimpse of TripTracker's interface and functionalities.


## Team
Our team is composed of 7 EPFL students. We are passionate about creating innovative solutions to real-world problems and are excited to share our project with you.
The team members are:
- Cléo Renaud 
- Jérémy Barghorn
- Loïc Misenta
- Théo Schifferli
- Pol Fuentes Camacho
- Jérémy Chaverot
- Léopold Galhaud

## Contact
If you have any questions or feedback, feel free to reach out to us at
triptracker@lastartup.com