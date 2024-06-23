import 'package:flutter/material.dart';

class ChatController extends ScrollController {
  final Map<num, num> heights = {};

  void updateHeight(num? key, num value) {
    if (key == null) return;
    heights[key] = value;
  }
}
