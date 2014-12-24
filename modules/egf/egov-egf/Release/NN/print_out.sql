CREATE OR REPLACE PROCEDURE print_out(
  IN_TEXT        VARCHAR2,
  IN_TEXT_LENGTH NUMBER   DEFAULT 255,
  IN_DIVIDER     VARCHAR2 DEFAULT CHR(32),
  IN_NEW_LINE    VARCHAR2 DEFAULT NULL)
IS
  lv_print_text        VARCHAR2(32767);
  ln_position          PLS_INTEGER;
  ln_divider_position  PLS_INTEGER;
  ln_total_printed     PLS_INTEGER;
  ln_string_length     PLS_INTEGER;

  PROCEDURE printText (IN_PRINT VARCHAR2)
  IS
  BEGIN
    dbms_output.put_line( IN_PRINT );
  END printText;

BEGIN

  IF IN_TEXT_LENGTH >255
  THEN
    ln_string_length := 255;
  ELSE
    ln_string_length := IN_TEXT_LENGTH;
  END IF;

  IF LENGTHB(IN_TEXT) <=IN_TEXT_LENGTH
  THEN
    printText(IN_TEXT);
  ELSE

    ln_position := 1;
    ln_total_printed := 0;

    LOOP
      lv_print_text := SUBSTR( IN_TEXT,ln_position, ln_string_length );

      IF IN_NEW_LINE IS NULL
      THEN
        ln_divider_position := INSTR(lv_print_text, IN_DIVIDER, -1);  -- get position for the last divider
      ELSE
        ln_divider_position := INSTR(lv_print_text, IN_NEW_LINE, -1);
        IF ln_divider_position = 0
        THEN
          ln_divider_position := INSTR(lv_print_text, IN_DIVIDER, -1);  -- get position for the last divider
        END IF;
      END IF;

      IF ln_divider_position = 0
      THEN
        ln_divider_position := ln_string_length;
      END IF;

      IF ln_divider_position <=ln_string_length
      THEN
        lv_print_text := SUBSTR( IN_TEXT, ln_position, ln_divider_position);

        IF length( lv_print_text ) <> lengthb(lv_print_text)
        THEN
          ln_divider_position := ln_divider_position-(lengthb(lv_print_text)-length( lv_print_text ));
          lv_print_text := SUBSTR( IN_TEXT, ln_position, ln_divider_position);

          IF IN_NEW_LINE IS NULL
          THEN
            ln_divider_position := INSTR(lv_print_text, IN_DIVIDER, -1);  -- get position for the last divider
          ELSE
            ln_divider_position := INSTR(lv_print_text, IN_NEW_LINE, -1);
            IF ln_divider_position = 0
            THEN
              ln_divider_position := INSTR(lv_print_text, IN_DIVIDER, -1);  -- get position for the last divider
            END IF;
          END IF;

          IF ln_divider_position = 0
          THEN
            ln_divider_position := ln_string_length-(lengthb(lv_print_text)-length( lv_print_text ));
          END IF;

          lv_print_text := SUBSTR( IN_TEXT, ln_position, ln_divider_position);
        END IF;

        IF ln_divider_position = 0
        THEN
          ln_divider_position := ln_string_length;
        END IF;

        ln_position := ln_position+ln_divider_position;
      END IF;

      ln_total_printed := ln_total_printed+LENGTHB(lv_print_text);

      lv_print_text := TRIM( lv_print_text );
      --dbms_output.put_line('***');
      printText(lv_print_text);

      EXIT WHEN ln_position >= LENGTH(TRIM(IN_TEXT));

    END LOOP;

    IF ln_position <ln_total_printed  -- printed not everything
    THEN
      printText(substr( IN_TEXT, ln_position, ln_total_printed ));
    END IF;

  END IF;
EXCEPTION
  WHEN others
  THEN
    dbms_output.put_line( 'ERROR :'||SQLERRM );
    dbms_output.put_line( 'ln_position: '||ln_position );
    dbms_output.put_line( 'ln_divider_position: '||ln_divider_position );

END print_out;
/
exit;