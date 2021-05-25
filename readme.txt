So this project, at the time of writing these instructions, (5/24) is at best 45% done,
and the concept for the finished project was quite complicated and esoteric at the outset,
so basically what you see is a bunch of debug output.

So...
What is the project?
well this is Ziker 4, an attempt at making a 'programming language' I've tried (and at least nominally succeeded)
before, and this is my 4th attempt.
Currently, being not quite half done at the time of writing (though I doubt that will change by the time you are reading this)
It only understands some basic 'control flow' statements (ifs, loops) and (partially) the types of certain things
(strings, numbers, characters(though most other things are just put in a collective bucket, and everyhing else isn't validated, so a 'number' which
is just a decimal point is fine by it, as well as characters that are multiple characters, among other things)) 
It probably sounds pretty underwhelming, but me submitting this isn't the end of the project; I intend to finish it over the summer.
It was just generally more than I anticipated, even though I started around a week early.
I should not be in the project time line estimation business.

How do I use it?
Well, maybe most importantly is that, since the project didn't get finished, a few of the requirements couldn't exactly be emmbeded into the program.
Namely reading and writing to a file, and formatting strings. I have included this functionality for the sake of meething those requirements. When you input nothing,
and simply press enter, a file is written to, and an arbitrary number is formatted. I wished I could've gotten those working in the context of the project, but unfortunately
the features that make those possible require an already fully working language, in my eyes at least.

But, aside from that, what can the program do?
you can type one or multiple 'statements' seperated by semi colons in one or multiple lines of input
ex:

// one line
:> statement 1; statement 2; statement 3
:< output

// multiple lines
:> statement 1;
:> statement 2;
:> statement 3
:< output

Ziker is also capable of identifying various types of 'tokens' in each statement tokens it cannot yet
identify are put under the type 'proto-type', and these types include variables, keywords, and function calls
these types are also not validated. "." is a number, a string or character doesn't need an end quote, characters can be multiple characters,
and operators are any symbol, regardless if they are actually defined.

Ziker can at times recognize statements starting with if, end, else, or while as special statements, and
organizes the code it reads in particular ways. if these tokens aren't in the beginning of the text, they are ignored.

I think that's about it...
have at it, I guess?
