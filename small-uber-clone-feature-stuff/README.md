![Heetch](heetch.png)

> Enjoy your night out

## Test
Given a webservice accepting your current coordinates as parameters and returning a list of drivers (in Paris) with their coordinates :

- Display all the drivers on a map
- Refresh their locations every 5 seconds
- Display the nearest driver's distance from your location

Taping a button should re-center the map around you and the closest driver
(the screen should be at the center between you and the closest driver).

#### Workflow
*The application must be hosted on this Github repository.*

- Commit on master the skeleton of your app
- Create a new branch
- Commit and push to this branch
- Submit a pull request once you are done


The release **must**:

- Have a beautiful name
- Include tests

#### Resources

**Webservice url**

http://hiring.heetch.com/mobile/coordinates

**Route**

```
PUT /coordinates
```

**Headers**

```
Accept: application/json
Content-Type: application/json
```

**Body**

```
{
  "latitude": "48.858181",
  "longitude": "2.348090"
}
```

#### Bonus

- Add an app icon which rocks!
- Add some Reactive code
- Add whatever you think is necessary to make the app awesome!
