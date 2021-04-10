# Arabic Text Indexer

## Overview:  

This project creates index files for Saudi Stock Market Arabic and English announcements' texts extracted from [Tadawul](https://www.saudiexchange.sa/wps/portal) using [Tadawul Crawler](https://github.com/Mhz95/Tadawul-Crawler).  

### Processing:
#### •	Tokenization:
For each given document: Both Arabic and English words are tokenized,   
all spaces and all punctuations are removed using the following regular expression: `[^\\p{L}\\p{Z}]`.  
The tokens are then stored in a list for further processing.

#### •	Stopping:
For each given token in the list of tokens: We compare with the provided stop word lists in the Resources, both Arabic and English lists, if a match found, we remove the token. Finally, a new list of the tokens is stored for the next procedure.
#### •	Stemming:
We employed Snowball stemmers to stem each token, the stemmers are provided in the Resources for both Arabic and English words.

### Indexing strategy:
•	We implemented an inverted index and store it in the hard desk for faster recovery. We used each document’s date and time as an ID in a form of UNIX timestamp.
#### •	The index is formatted as a JSON file. And structured as follows:
```
Term : Object {
		Array [
			Document {
				Array [
					Term {
						Position,
						OriginalTerm,
						Type
					},
					Term {
						…
					},
					]
				},
			…
			Document {
				…
			},
			]
}
```
#### •	Example of the index file:
<p align="center">
<img src="https://github.com/Mhz95/Arabic-Text-Indexer/blob/main/img.png" width="500">
 </p>
•	We indexed the 3 stock data once, and then each time we read the desired index for applying a procedure such as: Getting the Top 10 frequent words, Getting all words counted and Looking for a specific word in the text (Search). 

### Complications:
•	Some documents do not have a date or time, and since we depend on those as identifiers, we handled the missing values by taking the current time and replaced the last three digits with a random number.  


> Developed as part of a Computer Science MSc course   
> Supervisor: Dr. Mohammad Alsulmi   
> Course: CSC569: Selected Topics in AI   
> King Saud university    
> April 2021
