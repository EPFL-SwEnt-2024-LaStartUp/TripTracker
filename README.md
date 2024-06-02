# TripTracker

**TripTracker is a mobile application designed to enhance the experience of exploring a city on foot by providing pleasant walking itineraries around interesting spots. Record and share your own walking paths, discover paths created by others, connect with your friends, and stay updated with trending routes. It’s the ultimate travel companion that turns every trip into a memorable journey shared with the community!**

## Overview 

The TripTracker application goal is to simplify the way people explore places on foot. By providing users with enjoyable walking itineraries around interesting spots, this application ensures that users make the most of their trips. The application also allows users to record their own paths as they walk and add points of interest on the way. They can upload photos and descriptions of the spots they visit while recording their paths and when saving the path they can add a title and a description to it.

The application also offers a community aspect, as the recorded paths can be shared with other users of TripTracker. This way, tourists and locals alike can benefit from the experiences of others and discover new places to explore. It is also a social platform where users can look up their friends’ profiles and start following them. By connecting with their friends, users can easily stay updated with the latest paths created by their friends and share their own paths with them.

Users can save the paths they like to easily find them later in their favorites. The itineraries added to this folder are automatically downloaded to enable offline access. This way, users can view the paths they have saved offline and enjoy their trip without worrying about an internet connection.

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
