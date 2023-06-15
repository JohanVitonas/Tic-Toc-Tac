Cell[][] board;       // Declaración de una matriz para representar el tablero del juego
int cols = 3;         // Número de columnas del tablero
int rows = 3;         // Número de filas del tablero
int player = 0;       // Variable para llevar el seguimiento del jugador actual (0 para jugador 1)
int win = 0;          // Variable para indicar el estado del juego (0: sin ganador, 1: jugador 1 gana, 2: jugador 2 gana)
int game = 0;         // Variable para indicar el estado del juego (0: juego no iniciado, 1: juego en curso)
int full = 9;         // Variable para llevar el seguimiento de la cantidad de celdas vacías restantes en el tablero

void setup() {
  size(400, 400);     // Establece el tamaño de la ventana del juego
  smooth();           // Activa el suavizado de bordes
  board = new Cell[cols][rows];  // Inicializa la matriz del tablero con objetos Cell
  // Crea una instancia de la clase Cell para cada celda del tablero
  for (int i = 0; i < cols; i++) {
    for (int j = 0; j < rows; j++) {
      board[i][j] = new Cell(width/3*i, height/3*j, width/3, height/3);
    }
  }
}

void draw() {
  background(0);  // Establece el fondo de la ventana como negro

  // Mensaje de inicio del juego
  if (game == 0) {
    fill(255);             // Establece el color de relleno en blanco
    textSize(20);          // Establece el tamaño de texto en 20
    text("Press ENTER to Start", width/2-width/4, height/2); // Muestra el mensaje en el centro de la ventana
  }

  // Inicia el juego
  if (game == 1) {
    checkGame();  // Verifica si hay un ganador
    // Recorre la matriz del tablero y muestra cada celda
    for (int i = 0; i < cols; i++) {
      for (int j = 0; j < rows; j++) {
        board[i][j].display();
      }
    }
  }
}

// Función para manejar el clic del mouse
void mousePressed() {
  if (game == 1) {
    if (win == 0) {
      // Recorre la matriz del tablero y maneja el clic en cada celda
      for (int i = 0; i < cols; i++) {
        for (int j = 0; j < rows; j++) {
          board[i][j].click(mouseX, mouseY);
        }
      }
    }
  }
}

// Función para manejar la tecla presionada
void keyPressed() {
  if (game == 0) {
    if (key == ENTER) {
      game = 1;     // Inicia el juego al presionar la tecla ENTER
      full = 9;     // Reinicia el contador de celdas vacías
    }
  } else if (game == 1 && win == 0 && full == 0) {
    if (key == ENTER) {
      game = 0;     // Reinicia el juego al presionar la tecla ENTER
      // Restablece todas las celdas del tablero, el estado de ganador y el contador de celdas vacías
      for (int i = 0; i < cols; i++) {
        for (int j = 0; j < rows; j++) {
          board[i][j].clean();
          win = 0;
          full = 9;
        }
      }
    }
  } else if (game == 1 && (win == 1 || win == 2)) {
    if (key == ENTER) {
      game = 0;     // Reinicia el juego al presionar la tecla ENTER
      // Restablece todas las celdas del tablero, el estado de ganador y el contador de celdas vacías
      for (int i = 0; i < cols; i++) {
        for (int j = 0; j < rows; j++) {
          board[i][j].clean();
          win = 0;
          full = 9;
        }
      }
    }
  }
}

// Función para verificar si hay un ganador
void checkGame() {
  int row = 0;

  // Verifica si hay tres celdas del mismo jugador en línea en las filas y columnas
  for (int col = 0; col < cols; col++) {
    if (board[col][row].checkState() == 1 && board[col][row+1].checkState() == 1 && board[col][row+2].checkState() == 1) {
      win = 1;  // Jugador 1 gana
    } else if (board[row][col].checkState() == 1 && board[row+1][col].checkState() == 1 && board[row+2][col].checkState() == 1) {
      win = 1;  // Jugador 1 gana
    } else if (board[row][col].checkState() == 2 && board[row+1][col].checkState() == 2 && board[row+2][col].checkState() == 2) {
      win = 2;  // Jugador 2 gana
    } else if (board[col][row].checkState() == 2 && board[col][row+1].checkState() == 2 && board[col][row+2].checkState() == 2) {
      win = 2;  // Jugador 2 gana
    }
  }

  // Verifica si hay tres celdas del mismo jugador en las diagonales
  if (board[row][row].checkState() == 1 && board[row+1][row+1].checkState() == 1 && board[row+2][row+2].checkState() == 1) {
    win = 1;  // Jugador 1 gana
  } else if (board[row][row].checkState() == 2 && board[row+1][row+1].checkState() == 2 && board[row+2][row+2].checkState() == 2) {
    win = 2;  // Jugador 2 gana
  } else if (board[0][row+2].checkState() == 1 && board[1][row+1].checkState() == 1 && board[2][row].checkState() == 1) {
    win = 1;  // Jugador 1 gana
  } else if (board[0][row+2].checkState() == 2 && board[1][row+1].checkState() == 2 && board[2][row].checkState() == 2) {
    win = 2;  // Jugador 2 gana
  }

  // Verifica si todas las celdas están ocupadas y no hay ganador
  if (full == 0 && win == 0) {
    win = 3;  // Empate
  }
}

// Clase para representar una celda del tablero
class Cell {
  float x, y;      // Posición de la celda
  float w, h;      // Ancho y alto de la celda
  int state = 0;   // Estado de la celda (0: vacía, 1: jugador 1, 2: jugador 2)

  Cell(float x, float y, float w, float h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
  }

  // Función para mostrar la celda en la ventana del juego
  void display() {
    // Dibuja el borde de la celda
    stroke(255);
    noFill();
    rect(x, y, w, h);

    // Dibuja el símbolo del jugador en la celda
    if (state == 1) {
      // Jugador 1 (X)
      line(x + w/4, y + h/4, x + 3*w/4, y + 3*h/4);
      line(x + w/4, y + 3*h/4, x + 3*w/4, y + h/4);
    } else if (state == 2) {
      // Jugador 2 (O)
      ellipse(x + w/2, y + h/2, w/2, h/2);
    }
  }

  // Función para manejar el clic en la celda
  void click(float mouseX, float mouseY) {
    if (mouseX > x && mouseX < x + w && mouseY > y && mouseY < y + h && state == 0) {
      if (player == 0) {
        state = 1;    // Asigna la celda al jugador 1
        player = 1;   // Cambia al jugador 2
      } else {
        state = 2;    // Asigna la celda al jugador 2
        player = 0;   // Cambia al jugador 1
      }
      full--;         // Reduce el contador de celdas vacías
    }
  }

  // Función para verificar el estado de la celda
  int checkState() {
    return state;
  }

  // Función para limpiar el estado de la celda
  void clean() {
    state = 0;
  }
}
