import io.Console;
import list.RealList;
import util.Num;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Дублирование положительных чисел ===");
        System.out.println("(введите числа через пробел или запятую)");

        RealList list = null;

        while (list == null) {
            System.out.print("Введите список: ");
            String line = Num.trim(Console.readLine());

            if (line.length() == 0) {
                System.out.println("  [!] Пусто — попробуйте снова.");
                continue;
            }

            RealList candidate = new RealList();
            int bad = 0;
            String[] parts = Num.split(line);

            for (int i = 0; i < parts.length; i++) {
                if (Num.isDouble(parts[i])) candidate.addLast(Num.parse(parts[i]));
                else bad++;
            }

            if (candidate.isEmpty()) {
                System.out.println("  [!] Нет ни одного корректного числа — попробуйте снова.");
                continue;
            }
            if (bad > 0) System.out.println("  [i] Пропущено некорректных: " + bad);
            list = candidate;
        }

        System.out.println();
        System.out.println("Исходный список:");
        System.out.println("  " + list + "  (" + list.size() + " эл.)");

        list.duplicatePositive();

        System.out.println();
        System.out.println("После дублирования положительных:");
        System.out.println("  " + list + "  (" + list.size() + " эл.)");
    }
}