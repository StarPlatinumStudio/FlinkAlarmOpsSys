## What makes a good README?
The following is the recommended format for the READMEs appearing on Grafana.net. 

#### Screenshots
**Important**: Screenshots are not part of the README, but are an essential part of presenting your plugin successfully. Screenshots are packaged within the /src directory of the plugin itself and specified within plugin.json; more information can be found [here](#). 

-------

![img](https://circleci.com/gh/raintank/worldping-app.svg?style=shield&circle-token=:circle-token) *Build icons are great if you have it*

The README should start with a clear, long-form description of what your Grafana plugin does. The short description appears above under the plugin title, so you can be a bit more verbose with the purpose of the app, what it's for and why you should use it.

Remember that sometimes, people downloading this app may not know what underlying service or technology is, and this is an opportunity to explain it. 


## Live Demo

A live demo is always the best way to show off your plugin. You can link off to a snapshot or live demo hosted on your Grafana. 

## Features

This next section should talk about the features of the plugin within Grafana. 

### Biggest Features
The larger features can demand their own section. 


```
You can add code blocks.
```

Create | Tables
------------ | -------------
1 | 2
A | B

Or add images

![Image Title](http://grafana.org/assets/img/blog/mixed_styles.png)

All standard markdown is supported. For a nice reference, view [Github's Mastering Markdown](https://guides.github.com/features/mastering-markdown/) article. 


### Functionality within Grafana
If the feature differs slightly within Grafana, or offers additional functionality not previously available, this is a great place to mention that. 

### Additional Features

For non-headline features, bulleted lists can be effective and concise way to increase skimmability.

- Goes great on toast.
- Best thing since sliced bread. 
- Bears, beets, Battlestar Galactica. 


## External Dependencies

The Installation tab only covers the installation of the plugin itself, so if your plugin relies on external technologies, this is a great place to explain what else a user may need to do. 

If the set instructions are more complex, and more coupled with specific actions within the application (for example, adding a `device` within a UI page), it can be more effective to build those instructions either directly into the app or maintain external documentation. 



#### Changelog

The README can also include a trailing list of changes for recent versions. 

##### v1.0.6
- Updated the knickerbocker widget to accept multi-input threading. 
- Dialed up the down.
- Righted the left

##### v1.0.5
- Fixed bugs introduced in 1.0.4.
- Introduced bugs that will be fixed in 1.0.6
