import sys

def format_code(lines):
    formatted_lines = []
    indentation_level = 0

    for line in lines:
        stripped_line = line.strip()

        if stripped_line.endswith('{'):
            formatted_lines.append(' ' * indentation_level + stripped_line)
            indentation_level += 4
        elif stripped_line.startswith('}'):
            indentation_level -= 4
            formatted_lines.append(' ' * indentation_level + stripped_line)
        else:
            formatted_lines.append(' ' * indentation_level + stripped_line)

    return '\n'.join(formatted_lines)

if __name__ == "__main__":
    # Read all lines from standard input
    lines = sys.stdin.read().splitlines()
    # Format code
    formatted_code = format_code(lines)
    # Output the formatted code
    print(formatted_code)
