# Forismatic Client
## _GUI-based quote generator_

[![N|Solid](https://hsto.org/web/c90/c58/4ad/c90c584ad04e44249bb11d97461ee0e3.png)]

Forismatic collects the most inspiring expressions of mankind.

There are not any catalogues of phrases or lists of authors on the site, full of sages and philosophers’ thoughts, writers and outstanding people’s aphorisms. We don’t believe in a random choice. Only you can guide your destiny. Just listen to yourself and one of the most inspiring expressions of mankind will be the sign for you.

# This is how it looks like:
[![N|Solid](https://raw.githubusercontent.com/meirbeking/forismaticCLient/images/main.png)]

Tools I used while making the project:

- JavaFX
- Forismatic.com API
- Brain

## Features

- Lets you see manifold types of quotes by famous writer, scientists and genuieses..
- Easily navigate between quotes you've seen
- Choose suitable languages (English, Russian, and others are planned to be included :))
- Lets you bookmark your favourite quotes
- Export bookmarked quotes as txt file in your local machine

On this project, I demonstrated my knowledge on Java OOP using different type of java features including both procedural and functional features. Learning OOP once was only a dream for me, but now I can see the accomplished goal ;)

> A goal is a dream with a deadline. 
> (C) Napoleon Hill

## Further improvements in long run:

Currently, in my to-do list:

- [Emailing quotes] - Adding the option where user can send particular quote to his or some other emails
- [SMS quotes] - Adding functionality where user can send SMS quotes to a certain telephone number (use API)
- [Import quotes] - Feature of adding quotes by users themselves
- [Adding pictures] - of famous people who told the quote

And of course Forismatic Client is open source with a public repository on GitHub. 
If anyone who would like to make contributions, you're welcome!

## Modules and API

Forismatic is currently extended with the following module and API.

| Library | LINK |
| ------ | ------ |
| JavaFX | https://openjfx.io/ |
| Forismatic.com API |https://forismatic.com/en/api/|

## Development

Setting.java class is defined as a singleton class. It was not in scope of the requirements for the endterm project, but still I used it to send necessary parameters within JavaFX forms since there was not official method for handling such operations. Those parameters: language and dropBookmarks command. These parameters were sent from preferences form to main form.

Other than that, Main.java contains classes for driver code and also for database connection. I used PostgreSQL as database management system driver, but you may choose any type of RDBMS systems of your choice.

Finally, we have our sample.fxml and preferences.fxml files. These files are defined as the frontend part of the project, particularly javafx forms.

## License

MIT

**Free Software**
