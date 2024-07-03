import 'package:flutter/material.dart';

class AddBodyOptionItem extends StatelessWidget {
  const AddBodyOptionItem(
      {super.key, this.onTap, required this.text, required this.icon});

  final GestureTapCallback? onTap;

  final IconData icon;

  final String text;

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: onTap,
      child: Container(
        color: Theme.of(context).colorScheme.surfaceContainerHigh,
        padding: const EdgeInsets.all(16),
        child: Row(
          children: [
            Icon(
              icon,
              size: 28,
            ),
            const SizedBox(
              width: 16,
            ),
            Text(
              text,
              style: const TextStyle(
                fontSize: 20,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
