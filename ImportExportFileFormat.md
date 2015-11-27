# Introduction #

Cardwall for Open Wonderland provides an import/export facility to preserve data, maintain historical archives and to transfer walls between worlds. The file format can also be used to develop templates for card walls, allowing the user to change the number of sections, columns and rows along with the title for each section.


# Details #

### Format ###

The file format is comma separated values (CSV).  For a cardwall file to be imported back into an Open Wonderland world, the file extension should be .cardwall.

Column order is not enforced, however the grouping and order of the rows is strictly defined.

The first row contains the header and these must match the labels described below or an error will occur. (Presently there is no formal error reporting and the user will need to refer to the console log for some hint as to the cause of the error.)

The present importer will handle files from previous versions of cardwall and use the default definition of the cardwall layout.

## Specifications ##

### Columns ###
|Column Header|Comment|
|:------------|:------|
|recordType   |Defines the type of data in the row - grouping of record types is order dependent |
|section      |       |
|column       |       |
|relativeColumn|       |
|row          |       |
|colour       | Note the spelling (UK/CAN)|
|title        |       |
|detail       |       |
|person       |       |
|points       |       |

### Row Types ###
  * cardWall
  * section
  * row (Not currently supported)
  * card


## Record (Row) Type descriptions ##

Note:

  * all numerics are positive integers except for a) colour (can be negative)  b) in a card record type -1 for row or column when the card is in the archive of the section
  * record type labels are case sensitive

### Record Type:cardWall ###

Only one and must follow the header row.

|Column Header|Description|Required|Type|Values|Comment|
|:------------|:----------|:-------|:---|:-----|:------|
|recordType   |           | Y      | Text | cardWall |       |
|section      | Number of sections in this cardwall | Y      | Numeric |      |       |
|column       | Number of columns in this card wall | Y      | Numeric  |      |       |
|relativeColumn| Not Used - values ignored |        |    |      |       |
|row          | Number of rows in this card wall | Y      | Numeric |      |       |
|colour       | Not Used - values ignored |        |    |      |       |
|title        | Name of the Card wall | Y      | Text |      |       |
|detail       | Not Used - values ignored |        |    |      |       |
|person       | Not Used - values ignored |        |    |      |       |
|points       | Not Used - values ignored |        |    |      |       |

### Record Type:section ###

One row for each section and must follow the cardwall row.

|Column Header|Description|Required|Type|Values|Comment|
|:------------|:----------|:-------|:---|:-----|:------|
|recordType   |           | Y      | Text | section |       |
|section      | Number of the section | Y      | Numeric |      |       |
|column       | Number of columns in this section | Y      | Numeric |      |       |
|relativeColumn| Not Used - values ignored |        |    |      |       |
|row          | Not Used - values ignored |        |    |      |       |
|colour       | Not Used - values ignored |        |    |      |       |
|title        | Section title | Y      | Text |      |       |
|detail       | Not Used - values ignored |        |    |      |       |
|person       | Not Used - values ignored |        |    |      |       |
|points       | Not Used - values ignored |        |    |      |       |

### Record Type:row ###

Not implemented in Ver 0.0.3 - DO NOT INCLUDE IN THE FILE

One for any row where the default values are overridden

|Column Header|Description|Required|Type|Values|Comment|
|:------------|:----------|:-------|:---|:-----|:------|
|recordType   |           |        |    |      |       |
|section      |           |        |    |      |       |
|column       |           |        |    |      |       |
|relativeColumn|           |        |    |      |       |
|row          |           |        |    |      |       |
|colour       |           |        |    |      |       |
|title        |           |        |    |      |       |
|detail       |           |        |    |      |       |
|person       |           |        |    |      |       |
|points       |           |        |    |      |       |

### Record Type:card ###

One for each card and must follow the section rows. (Once row record types are implemented, cards will follow the row records.)

|Column Header|Description|Required|Type|Values|Comment|
|:------------|:----------|:-------|:---|:-----|:------|
|recordType   |           | Y      | Text | card |       |
|section      | The section where the card is located | Y      |    |      |       |
|column       | Absolute column | Y      | Numeric | range: 0 to the number of columns - 1 OR -1 for an archived card|       |
|relativeColumn| Not Used - values ignored |        |    |      |       |
|row          | Row       |        |    | range: 0 to the number of rows - 1 OR -1 for an archived card |       |
|colour       | Colour value as a base 10 integer  | Y      | Numeric |      | 0 represents the default colour. In a later version a more standard representation of colour will be implemented |
|title        | Heading of card | N      | Text |      |       |
|detail       | Body of card | N      | Text |      |       |
|person       | Text for the person field on the card | N      | Text |      |       |
|points       | Text for the points field on the card | N      | Text |      |       |